package live.qsmc.quipt.tests;

import live.qsmc.quipt.fabric.blocks.abstracts.QuiptFullBlock;
import live.qsmc.quipt.fabric.blocks.abstracts.properties.BlockEnumProperty;
import live.qsmc.quipt.fabric.blocks.abstracts.properties.BlockIntProperty;
import live.qsmc.quipt.fabric.blocks.abstracts.properties.BlockProperty;

public class TestBlock extends QuiptFullBlock {
    public TestBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockProperty<?>[] properties() {
        BlockProperty<?>[] properties = new BlockProperty[2];
        properties[0] = BlockEnumProperty.of("test_enum", TestEnum.class, TestEnum.TEST1);
        properties[1] = BlockIntProperty.of("test_int", 0, 10, 5);
        return properties;
    }

}
