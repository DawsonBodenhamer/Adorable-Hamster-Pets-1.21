package net.dawson.adorablehamsterpets.world.gen;

public class ModWorldGeneration {
    public static void generateModWorldGen() {
        ModOreGeneration.generateOres();
        ModTreeGeneration.generateTrees();

        ModEntitySpawns.addSpawns();


    }
}
