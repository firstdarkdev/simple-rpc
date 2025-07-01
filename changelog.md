## Requires CraterLib

- Temporary Documentation: [Simple RPC Beta](https://srpcbeta.fdd-docs.com/)
- Source Code: [GitHub](https://github.com/firstdarkdev/simple-rpc)
- Report Issues: [GitHub](https://github.com/firstdarkdev/simple-rpc/issues)

*This jar works on ALL minecraft versions from 1.18.2 up until 1.21.7. Forge is only supported until 1.20.4*

### WARNING: Configs from older versions of Simple RPC (3.x and below) are NOT compatible with these versions. You will need to redo your config.

**Bug Fixes**:

- Fixed Windows AccessDenied errors not being caught when discord is running as Administrator
- Fixed Linux ignoring a bunch of rpc events
- Fixed Linux not detecting user switching
- Fixed Reconnect handler logic not working
- Fixed Snap/FlatPak versions of discord not being detected on Linux
- Fixed RPC event not updating when switching between pause menu, or joining worlds - Issue #102