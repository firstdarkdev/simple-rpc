## Requires CraterLib

*This jar works on ALL minecraft versions from 1.18.2 up until 1.21.4. Forge is only supported until 1.20.4*

### WARNING: Configs from older versions of Simple RPC are NOT compatible with these versions. You will need to redo your config. Our online editor still supports it.

**Bug Fixes**:

- Greatly improved the performance of the placeholder system. No more stuttering and fps drops while resolving placeholders
- Placeholders can now fail individually. For example: player count fails, all the other server placeholders doesn't break
- RPC still showing you as in-game, when you get kicked from a server

**New Features**:

- Mod is now fully open source, licensed under MIT
- Developer API for developers to add their own RPC, or control our RPCs
- Multiple RPCs per event. This means, for each event, like Single Player, you can have multiple rpcs that will be chosen at random
- You can now add buttons to Server Entries
- Full-featured in-game config screen
- Paused State RPC
- Added in-game notice if you have CraftPresence and Simple RPC installed at the same time

**Changes**:

- Switched from using the old C++ based discord rpc, to a new, fully JAVA one. Should fix a lot of mac compat issues (I hope and pray it does)
