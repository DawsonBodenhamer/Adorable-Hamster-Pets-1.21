package net.dawson.adorablehamsterpets.integration.jade;

import net.dawson.adorablehamsterpets.block.custom.WildCucumberBushBlock;
import net.dawson.adorablehamsterpets.block.custom.WildGreenBeanBushBlock;
import net.dawson.adorablehamsterpets.entity.custom.HamsterEntity;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin // This annotation marks this class as a Jade plugin
public final class AHPJadePlugin implements IWailaPlugin {

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        // Block components
        registration.registerBlockComponent(WildBushComponentProvider.INSTANCE, WildCucumberBushBlock.class);
        registration.registerBlockComponent(WildBushComponentProvider.INSTANCE, WildGreenBeanBushBlock.class);

        // Entity component for Hamster debugging
        registration.registerEntityComponent(HamsterDebugComponentProvider.INSTANCE, HamsterEntity.class);
    }

    @Override
    public void register(IWailaCommonRegistration registration) {
        // This method is for server-side registration if needed.
        // We don't have any server-specific data for Jade in this case,
        // so this method can remain empty.
    }
}