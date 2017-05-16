package com.electrocomponents.model.data.catalogue.navigation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Image Enum.
 * @author Gillian Boyd
 */
public enum ImageEnum {
    /** Large Image type. */
    LARGE_IMAGE("Large Image"),

    /** Simple Image type. */
    SIMPLE_IMAGE("Simple Image"),

    /** Simple Image 60X60 pixels type. */
    SIMPLE_IMAGE_60("Simple Image 60 Pixels"),

    /** Thumbnail Image type. */
    THUMB_NAIL_IMAGE("Thumbnail Image"),

    /** Value for Money Image Type. */
    VFM_IMAGE("Vfm Image"),

    /** Value for General Image Type. */
    GENERAL_IMAGE("General Image"),

    /** Value for Search Image Type. */
    SEARCH_IMAGE("Search Image"),

    /** Value for Symbol Image Type. */
    SYMBOLS_IMAGE("Symbols Image"),

    /** Value for General Image Type. */
    BROWSE_IMAGE("Browse Image");

    /** Commons logging logger. */
    private static final Log LOG = LogFactory.getLog(ImageEnum.class);

    /** Symbol type value. */
    private final String imageTypeVal;

    /**
     * Private constructor, used to set the image type value.
     * @param imageTypeVal value of the type of image
     */
    private ImageEnum(final String imageTypeVal) {
        this.imageTypeVal = imageTypeVal;
    }

    /**
     * Returns a string representation of the value of the image.
     * @return imageTypeVal String value pointing to the image
     */
    public String getImageTypeVal() {
        return imageTypeVal;
    }

    /**
     * Returns the enum associated with the supplied value.
     * @param value given symbol value
     * @return ImageTypeEnum that match the given value parameter
     */
    public static ImageEnum value(final String value) {
        ImageEnum it = null;
        final ImageEnum[] values = values();
        for (final ImageEnum imageType : values) {
            if ((imageType.imageTypeVal).equals(value)) {
                it = imageType;
                break;
            }
        }
        if (it == null) {
            LOG.warn("Image Type Enum is null");
        }
        return it;
    }

}
