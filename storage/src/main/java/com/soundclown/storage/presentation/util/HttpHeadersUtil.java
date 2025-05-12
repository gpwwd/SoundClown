package com.soundclown.storage.presentation.util;

import com.soundclown.storage.domain.util.Range;
import lombok.experimental.UtilityClass;

@UtilityClass
public class HttpHeadersUtil {

    public String calculateContentLength(Range range, long fileSize) {
        return String.valueOf(range.getRangeEnd(fileSize) - range.getRangeStart() + 1);
    }

    public String constructContentRange(Range range, long fileSize) {
        return "bytes " + range.getRangeStart() + "-" + range.getRangeEnd(fileSize) + "/" + fileSize;
    }
} 