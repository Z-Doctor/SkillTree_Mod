package zdoctor.mcskilltree.skills;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.JSONUtils;
import zdoctor.mcskilltree.api.ISkillProperty;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public abstract class SkillProperty<T> implements ISkillProperty<T> {

    private String key;
    private Supplier<T> valueSupplier;
    // Will be based on the last value given

    public SkillProperty(@Nonnull String key, @Nonnull Supplier<T> value) {
        this.key = key;
        this.valueSupplier = value;
    }

    public static SkillProperty.IntProperty withDefault(String key, int value) {
        return new IntProperty(key, () -> value);
    }

    public static SkillProperty.BooleanProperty withDefault(String key, boolean value) {
        return new BooleanProperty(key, () -> value);
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public T getValue() {
        return valueSupplier.get();
    }

    @Override
    public void setValue(Supplier<Object> value) {
        this.valueSupplier = (Supplier<T>) value;
    }

    @Override
    public <E> E cast() {
        return (E) getValue();
    }

    public static class IntProperty extends SkillProperty<Integer> {
        public IntProperty(String key, Supplier<Integer> value) {
            super(key, value);
        }

        @Override
        public void writeTo(CompoundNBT tag) {
            tag.putInt(getKey(), getValue());
        }

        @Override
        public void from(CompoundNBT tag) {
            if (tag.contains(getKey())) {
                int value = tag.getInt(getKey());
                setValue(() -> value);
            }
        }

        @Override
        public IntProperty copy() {
            return SkillProperty.withDefault(getKey(), getValue());
        }

        @Override
        public JsonObject serialize() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(getKey(), getValue());
            return jsonObject;
        }

        @Override
        public IntProperty deserialize(JsonElement element) {
            if (element != null && !element.isJsonNull()) {
                JsonObject jsonObject = JSONUtils.getJsonObject(element, "property_object");
                int value = jsonObject.get(getKey()).getAsInt();
                return new IntProperty(getKey(), () -> value);
            }
            return null;
        }
    }

    public static class BooleanProperty extends SkillProperty<Boolean> {
        public BooleanProperty(String key, Supplier<Boolean> value) {
            super(key, value);
        }

        @Override
        public void writeTo(CompoundNBT tag) {
            tag.putBoolean(getKey(), getValue());
        }

        @Override
        public void from(CompoundNBT tag) {
            if (tag.contains(getKey())) {
                boolean value = tag.getBoolean(getKey());
                setValue(() -> value);
            }
        }

        @Override
        public BooleanProperty copy() {
            return SkillProperty.withDefault(getKey(), getValue());
        }

        @Override
        public JsonObject serialize() {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty(getKey(), getValue());
            return jsonObject;
        }

        @Override
        public BooleanProperty deserialize(JsonElement element) {
            if (element != null && !element.isJsonNull()) {
                JsonObject jsonObject = JSONUtils.getJsonObject(element, "property_object");
                boolean value = jsonObject.get(getKey()).getAsBoolean();
                return new BooleanProperty(getKey(), () -> value);
            }
            return null;
        }

    }
}

