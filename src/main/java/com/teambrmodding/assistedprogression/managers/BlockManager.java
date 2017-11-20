package com.teambrmodding.assistedprogression.managers;

import com.teambr.nucleus.annotation.RegisteringBlock;
import com.teambrmodding.assistedprogression.common.blocks.*;

/**
 * This file was created for AssistedProgression
 * <p>
 * AssistedProgression is licensed under the
 * Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License:
 * http://creativecommons.org/licenses/by-nc-sa/4.0/
 *
 * @author Paul Davis - pauljoda
 * @since 11/13/17
 */
public class BlockManager {

    /*******************************************************************************************************************
     * Block Variables                                                                                                 *
     *******************************************************************************************************************/

    @RegisteringBlock
    public static BlockCrafter blockCrafter = new BlockCrafter();

    @RegisteringBlock
    public static BlockPlayerPlate blockPlayerPlate = new BlockPlayerPlate();

    @RegisteringBlock
    public static BlockRedstoneClock blockRedstoneClock = new BlockRedstoneClock();

    @RegisteringBlock
    public static BlockGrinder blockGrinder = new BlockGrinder();

    @RegisteringBlock
    public static BlockKineticGenerator blockKineticGenerator = new BlockKineticGenerator();
}
