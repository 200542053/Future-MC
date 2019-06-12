package thedarkcolour.future.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.IStringSerializable;

public class BlockBell extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    //public static final EnumProperty<Attachment>

    public BlockBell() {
        super(Properties.create(Material.IRON));
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        //builder.add()
    }

    public enum Attachment implements IStringSerializable {
        ;

        private String name;
        Attachment(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}