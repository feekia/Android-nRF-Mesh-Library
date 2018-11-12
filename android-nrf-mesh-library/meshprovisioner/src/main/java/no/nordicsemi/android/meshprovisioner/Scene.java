package no.nordicsemi.android.meshprovisioner;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.RestrictTo;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

import no.nordicsemi.android.meshprovisioner.utils.MeshTypeConverters;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Class definitions for creating scenes in a mesh network
 */
@SuppressWarnings("unused")
@Entity(tableName = "scene",
        foreignKeys = @ForeignKey(entity = MeshNetwork.class,
                parentColumns = "mesh_uuid",
                childColumns = "mesh_uuid",
                onUpdate = CASCADE, onDelete = CASCADE),
        indices = @Index("mesh_uuid"))
public class Scene {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "mesh_uuid")
    @Expose
    private String meshUuid;

    @ColumnInfo(name = "name")
    @Expose
    private String name = "nRF Scene";

    @TypeConverters(MeshTypeConverters.class)
    @Expose
    private List<byte[]> addresses = new ArrayList<>();

    @ColumnInfo(name = "number")
    @Expose
    private int number;

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public Scene(final int id) {
        this.id = id;
    }

    @Ignore
    public Scene(){

    }

    public String getMeshUuid() {
        return meshUuid;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public void setMeshUuid(final String meshUuid) {
        this.meshUuid = meshUuid;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY)
    public int getId() {
        return id;
    }

    /**
     * Friendly name of the scene
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a friendly name to a scene
     *
     * @param name friendly name
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns the address of the scene
     *
     * @return 2 byte address
     */
    public List<byte[]> getAddresses() {
        return addresses;
    }

    /**
     * Sets addresses for this group
     *
     * @param addresses list of addresses
     */
    public void setAddresses(final List<byte[]> addresses) {
        this.addresses = addresses;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(final int number) {
        this.number = number;
    }
}
