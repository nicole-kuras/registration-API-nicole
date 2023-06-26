package pl.sda.registrationapi.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

public class PageReqUtils {

    public static PageRequest createPageReq(int page, int size,
                                            Sort.Direction sortDirection, String sortColumn) {
        PageRequest pageRequest;

        if (!StringUtils.hasText(sortColumn)) {
            pageRequest = PageRequest.of(page, size);
        } else {
            pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortColumn));
        }
        return pageRequest;
    }

}
