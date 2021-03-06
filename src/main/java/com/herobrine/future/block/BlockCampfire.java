package com.herobrine.future.block;

import com.herobrine.future.FutureMC;
import com.herobrine.future.init.FutureConfig;
import com.herobrine.future.sound.Sounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;
import java.util.Random;

public class BlockCampfire extends BlockBase {
    private static final AxisAlignedBB boundingBox = makeAABB(0,0,0, 16,7,16);//new AxisAlignedBB(0,0,0,1,0.375,1);
    public static final PropertyBool LIT = PropertyBool.create("lit");

    public BlockCampfire() {
        super(new BlockProperties("Campfire", Material.WOOD));
        setSoundType(SoundType.WOOD);
        setHardness(2.0F);
        this.setDefaultState(getBlockState().getBaseState().withProperty(LIT, true));
        setCreativeTab(FutureConfig.general.useVanillaTabs ? CreativeTabs.DECORATIONS : FutureMC.CREATIVE_TAB);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LIT);
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.getValue(LIT)) {
            if (rand.nextInt(10) == 0) {
                worldIn.playSound((double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), Sounds.CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 0.5F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.6F, false);
            }

            if (rand.nextInt(5) == 0) {
                for(int i = 0; i < rand.nextInt(1) + 1; ++i) {
                    worldIn.spawnParticle(EnumParticleTypes.LAVA, (double)((float)pos.getX() + 0.5F), (double)((float)pos.getY() + 0.5F), (double)((float)pos.getZ() + 0.5F), (double)(rand.nextFloat() / 2.0F), 5.0E-5D, (double)(rand.nextFloat() / 2.0F));
                }
            }

            //smoke(worldIn, pos, worldIn.getBlockState(pos.down()) == Blocks.HAY_BLOCK.getDefaultState(), false); // Make it cook?
        }
    }

    @Override
    public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
        player.addStat(Objects.requireNonNull(StatList.getBlockStats(this)));
        player.addExhaustion(0.005F);

        harvesters.set(player);
        int i = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
        this.dropBlockAsItem(worldIn, pos, state, i);
        harvesters.set(null);
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Items.COAL;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 1;
    }

    @Override
    public int quantityDropped(Random random) {
        return 2;
    }

    @Override
    public int getLightValue(IBlockState state) {
        return state.getValue(LIT) ? 15 : 0;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        Block block = worldIn.getBlockState(pos.up()).getBlock();
        return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(LIT, !(block instanceof BlockLiquid | block instanceof IFluidBlock));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = playerIn.getHeldItem(hand);
        Block block = worldIn.getBlockState(pos.up()).getBlock();
        if (worldIn.getBlockState(pos) != this.getDefaultState()) {
            if (stack.getItem() == Items.FLINT_AND_STEEL || stack.getItem() == Items.FIRE_CHARGE) {
                if (!(block instanceof BlockLiquid | block instanceof BlockFluidBase)) {
                    worldIn.setBlockState(pos, this.getBlockState().getBaseState().withProperty(LIT, true));
                    stack.damageItem(1, playerIn);
                }
            }
        }
        return false;
    }

    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        Block block = worldIn.getBlockState(pos.up()).getBlock();
        if (block instanceof BlockFluidBase | block instanceof BlockLiquid) {
            worldIn.setBlockState(pos, this.getBlockState().getBaseState().withProperty(LIT, false));
        }
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return boundingBox;
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if(FutureConfig.general.campfireDMG) {
            if (entityIn instanceof EntityLivingBase) {
                if(worldIn.getBlockState(pos) == this.getDefaultState()) {
                    entityIn.attackEntityFrom(DamageSource.IN_FIRE, 1.0F);
                }
            }
        }
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) { // Handles properties
        return this.getBlockState().getBaseState().withProperty(LIT, (meta == 1));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LIT) ? 1 : 0;
    }

    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    @Override public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.neighborChanged(state, worldIn, pos, worldIn.getBlockState(pos).getBlock(), pos);
    }

    @Override public boolean isBlockNormalCube(IBlockState state) { return false; }
    @Override public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) { return false; }
    @Override public boolean isFullBlock(IBlockState state) { return false; }
    @Override public boolean isOpaqueCube(IBlockState state) { return false; }
    @Override public boolean canPlaceTorchOnTop(IBlockState state, IBlockAccess world, BlockPos pos) { return false; }
    @Override public boolean isTopSolid(IBlockState state) { return false; }
/*
    public static void smoke(World worldIn, BlockPos pos, boolean hasHayBale, boolean isCooking) {
        Random random = worldIn.rand;
        EnumParticleType type = hasHayBale ? EnumParticleType.CAMPFIRE_SIGNAL_SMOKE : EnumParticleType.CAMPFIRE_COZY_SMOKE;

        ParticleSpawner.spawnParticle(type, worldIn, (double)pos.getX() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1), (double)pos.getY() + random.nextDouble() + random.nextDouble(), (double)pos.getZ() + 0.5D + random.nextDouble() / 3.0D * (double)(random.nextBoolean() ? 1 : -1), 0.0D, 0.07D, 0.0D);
        //if (false) { // is cooking? add in tile entity
        //    worldIn.addParticle(ParticleTypes.SMOKE, (double)pos.getX() + 0.25D + random.nextDouble() / 2.0D * (double)(random.nextBoolean() ? 1 : -1), (double)pos.getY() + 0.4D, (double)pos.getZ() + 0.25D + random.nextDouble() / 2.0D * (double)(random.nextBoolean() ? 1 : -1), 0.0D, 0.005D, 0.0D);
        //}

    }*/
}