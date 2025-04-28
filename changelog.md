## Requires CraterLib

- Temporary Documentation: [Simple RPC Beta](https://srpcbeta.fdd-docs.com/)
- Source Code: [GitHub](https://github.com/firstdarkdev/simple-rpc)
- Report Issues: [GitHub](https://github.com/firstdarkdev/simple-rpc/issues)

*This jar works on ALL minecraft versions from 1.18.2 up until 1.21.5. Forge is only supported until 1.20.4*

### WARNING: Configs from older versions of Simple RPC (3.x and below) are NOT compatible with these versions. You will need to redo your config.

**Bug Fixes**:

- Fix Custom Placeholders not being able to use built-in placeholders in them

**New Features**:

- Added `{{images.player.head}}` placeholder to return isometric player head image
- Added `{{player.uuid}}` placeholder to return the player UUID for use with avatar services
- Experimental RPC Image Server support, to allow using local images directly as RPC assets - [DOCS](https://srpcbeta.fdd-docs.com/experimental/localimages/)

**Changes**:

- Switched from using MC-HEADS to our own api for skins (mc-heads have had too many issues lately)