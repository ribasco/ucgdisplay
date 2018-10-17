package com.ibasco.ucgdisplay.core.input;

import java.util.Objects;

/**
 * @author Rafael Ibasco
 */
public class InputDeviceId {
    private static final int ID_BUS = 0;
    private static final int ID_VENDOR = 1;
    private static final int ID_PRODUCT = 2;
    private static final int ID_VERSION = 3;

    private short busId;
    private short vendorId;
    private short productId;
    private short productVersion;

    InputDeviceId(short[] ids) {
        busId = ids[ID_BUS];
        vendorId = ids[ID_VENDOR];
        productId = ids[ID_PRODUCT];
        productVersion = ids[ID_VERSION];
    }

    public short getBusId() {
        return busId;
    }

    public short getVendorId() {
        return vendorId;
    }

    public short getProductId() {
        return productId;
    }

    public short getProductVersion() {
        return productVersion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InputDeviceId that = (InputDeviceId) o;
        return busId == that.busId &&
                vendorId == that.vendorId &&
                productId == that.productId &&
                productVersion == that.productVersion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(busId, vendorId, productId, productVersion);
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("InputDeviceId{");
        sb.append("busId=").append(busId);
        sb.append(", vendorId=").append(vendorId);
        sb.append(", productId=").append(productId);
        sb.append(", productVersion=").append(productVersion);
        sb.append('}');
        return sb.toString();
    }
}
