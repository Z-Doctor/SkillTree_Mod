package zdoctor.mcskilltree.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundNBT;

import java.util.function.Supplier;

public interface ISkillProperty<T> {
    T getValue();

    String getKey();

    void setValue(Supplier<Object> value);

    <E> E cast();

    void writeTo(CompoundNBT tag);

    void from(CompoundNBT tag);

    ISkillProperty<T> copy();

    JsonObject serialize();

    ISkillProperty<?> deserialize(JsonElement element);
}