package fr.frinn.custommachinerymekanism.client.jei.heat;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fr.frinn.custommachinery.api.requirement.RequirementIOMode;

public record Heat(double amount, double chance, boolean isPerTick, RequirementIOMode mode) {
    public static final Codec<Heat> CODEC = RecordCodecBuilder.create(heatInstance ->
            heatInstance.group(
                    Codec.DOUBLE.fieldOf("amount").forGetter(Heat::amount),
                    Codec.DOUBLE.fieldOf("chance").forGetter(Heat::chance),
                    Codec.BOOL.fieldOf("isPerTick").forGetter(Heat::isPerTick),
                    RequirementIOMode.CODEC.codec().fieldOf("mode").forGetter(Heat::mode)
            ).apply(heatInstance, Heat::new)
    );
}
