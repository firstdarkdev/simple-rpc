package com.hypherionmc.simplerpc.util.rpcavatar;

import com.hypherionmc.simplerpc.RPCConstants;
import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.*;

/**
 * @author HypherionSA
 * An Image Server Client, to allow local image files to be used with Discord RPC
 */
public final class RPCImageServer {

    private final File iconsDirectory;
    private final APIClient apiClient;
    private Thread thread;
    @Getter private final String imageUrl;
    private final HashMap<String, String> fileMap = new HashMap<>();
    @Getter private boolean isUploading = false;

    public static final RPCImageServer INSTANCE = new RPCImageServer(new File("./config/simple-rpc/icons"), "https://rpcavatar.firstdark.dev");

    /**
     * Create a new instance of the Image Server client
     *
     * @param iconsDirectory The directory containing the icons that will be handled by the server
     * @param apiUrl The API URL the server is running on
     */
    RPCImageServer(File iconsDirectory, String apiUrl) {
        apiClient = new APIClient(apiUrl);
        this.imageUrl = apiUrl;
        this.iconsDirectory = iconsDirectory;
        iconsDirectory.mkdirs();
    }

    /**
     * Scan images folder for images, and check if they need to be uploaded to the image server
     */
    public void processImages() {
        File[] files = iconsDirectory.listFiles();
        fileMap.clear();

        if (files != null) {
            for (File file : Arrays.stream(files).filter(this::isValidImage).toList()) {
                try {
                    String hash = hashFile(file);
                    fileMap.put(file.getName(), hash);
                } catch (Exception e) {
                    RPCConstants.logger.error("Failed to process image file: {}", file.getName(), e);
                }
            }
        }

        // Check which images are missing from the server cache
        Set<String> hashes = apiClient.checkHashes(fileMap.values());
        List<String> filesToUpload = fileMap.entrySet().stream()
                .filter(entry -> !hashes.contains(entry.getValue()))
                .map(Map.Entry::getKey).toList();

        List<File> toUpload = filesToUpload.stream().map(f -> new File(iconsDirectory, f)).toList();

        // If any are missing, we upload them
        if (!toUpload.isEmpty()) {
            isUploading = true;
            if (thread != null) {
                thread.interrupt();
            }

            thread = new Thread(() -> {
                try {
                    apiClient.uploadFiles(toUpload);
                } catch (Exception e) {
                    RPCConstants.logger.error("Failed to upload images to RPC Image Server", e);
                }
                isUploading = false;
            });

            thread.start();
        }
    }

    /**
     * Get the image has of a local file, to be used with the RPC Image Server API
     *
     * @param fileName The name of the file to retrieve the hash for
     * @return The image has, or the original file name if the hash is missing
     */
    public String getHash(String fileName) {
        if (isUploading) {
            return fileName;
        }

        String hash = fileMap.getOrDefault(fileName, fileName);
        return (fileName.endsWith(".gif") || fileName.endsWith(".webp")) && !hash.equalsIgnoreCase(fileName) ? hash + ".gif" : hash;
    }

    /**
     * Calculate the SHA256 hash of a file
     *
     * @param file The file to calculate the hash of
     * @return The has of the file
     * @throws Exception Thrown when the hash cannot be calculated
     */
    private String hashFile(File file) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
        }

        byte[] hashBytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }

    /**
     * Just a rough check, to ensure only image files are handled, and that the file is smaller than 1MB
     *
     * @param file The file to check
     * @return True if the file passes the checks
     */
    private boolean isValidImage(File file) {
        try {
            String mime = Files.probeContentType(file.toPath());
            return mime != null && mime.startsWith("image/") && file.length() <= 1_000_000;
        } catch (Exception ignored) {}
        return false;
    }

}
