package com.hypherionmc.simplerpc.util.rpcavatar;

import com.google.gson.Gson;
import com.hypherionmc.simplerpc.RPCConstants;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

import java.io.File;
import java.util.*;

/**
 * @author HypherionSA
 * Simple API Client to communicate with our RPC image server
 */
final class APIClient {

    private final Gson gson = new Gson();
    private final String apiUrl;

    /**
     * Create a new API Client. Meant for internal use
     *
     * @param apiUrl The URL of the image server
     */
    APIClient(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    /**
     * Check file hashes against images cached on the Image server. This is used to help determine which files,
     * if any need to be uploaded
     *
     * @param hashes List of File SHA256 hashes
     * @return Set of Hashes on the server
     */
    @SuppressWarnings("unchecked")
    Set<String> checkHashes(Collection<String> hashes) {
        try {
            HttpResponse response = HttpRequest.post(apiUrl + "/check-hashes")
                    .header("Content-Type", "application/json; charset=utf-8")
                    .bodyText(gson.toJson(Collections.singletonMap("hashes", hashes)))
                    .send();

            if (response.statusCode() != 200 || response.bodyText() == null) {
                RPCConstants.logger.error("Failed to check local image hashes against API: {}", response.statusCode());
                return new HashSet<>();
            }

            return gson.fromJson(response.bodyText(), Set.class);
        } catch (Exception e) {
            RPCConstants.logger.error("Failed to check local image hashes against API", e);
        }

        return new HashSet<>();
    }

    /**
     * Upload missing files to the image server
     *
     * @param files List of files to upload.
     */
    void uploadFiles(List<File> files) {
        try {
            HttpRequest request = HttpRequest
                    .post(apiUrl + "/upload")
                    .multipart(true);

            for (File file : files) {
                request.form("files", file);
            }

            HttpResponse response = request.send();

            if (response.statusCode() != 200 || response.bodyText() == null) {
                RPCConstants.logger.error("Failed to upload local images to RPC Image Server: {}", response.statusCode());
                return;
            }

            StandardResponse reply = gson.fromJson(response.bodyText(), StandardResponse.class);

            if (reply.error) {
                RPCConstants.logger.error("Failed to upload local images to RPC Image Server: {}", reply.message);

                if (reply.data != null) {
                    RPCConstants.logger.error(reply.data);
                }
            } else {
                RPCConstants.logger.info("RPCImageServer: {}", reply.message);
            }
        } catch (Exception e) {
            RPCConstants.logger.error("Failed to upload local images to RPC Image Server", e);
        }
    }
}
