## Requires CraterLib

- Temporary Documentation: [Simple RPC Beta](https://srpcbeta.fdd-docs.com/)
- Source Code: [GitHub](https://github.com/firstdarkdev/simple-rpc)
- Report Issues: [GitHub](https://github.com/firstdarkdev/simple-rpc/issues)

*This jar works on ALL minecraft versions from 1.18.2 up until 1.21.4. Forge is only supported until 1.20.4*

### WARNING: Configs from older versions of Simple RPC (3.x and below) are NOT compatible with these versions. You will need to redo your config.

**Bug Fixes**:

- Fix Schema Version of ReplayMod config, which would cause a crash
- Fix `off_hand` and `main_hand` placeholders defaulting to 0, instead of "Air"
- Fix `health.percent` placeholder being reversed
- Fix `realm.world` placeholder falling back to Uppercase "World", instead of lowercase