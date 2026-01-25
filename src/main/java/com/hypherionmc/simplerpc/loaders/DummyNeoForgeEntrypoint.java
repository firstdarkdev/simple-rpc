package com.hypherionmc.simplerpc.loaders;

import com.hypherionmc.simplerpc.RPCConstants;
import lombok.NoArgsConstructor;
import net.minecraftforge.fml.common.Mod;

/**
 * @author HypherionSA
 * Dummy entrypoint for NeoForge. This is required, otherwise NeoForge crashes. Actual loading is handled by {@link com.hypherionmc.simplerpc.SimpleRPC}
 */
@Mod(RPCConstants.MOD_ID)
@NoArgsConstructor
public final class DummyNeoForgeEntrypoint {}
