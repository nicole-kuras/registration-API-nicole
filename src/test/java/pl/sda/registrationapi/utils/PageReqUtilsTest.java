package pl.sda.registrationapi.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

class PageReqUtilsTest {

    @Test
    void testCreatePageReqSortColumnDefined() {
        // given
        final int size = 10;
        final int pageNo = 0;
        final String sortColumn = "id";
        final Sort.Direction direction = Sort.Direction.ASC;
        final PageRequest expectedPageReq = PageRequest.of(pageNo, size, direction, sortColumn);

        // when
        final PageRequest actualPageReq = PageReqUtils.createPageReq(pageNo, size, direction, sortColumn);

        // then
        Assertions.assertEquals(expectedPageReq, actualPageReq);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void testCreatePageReqSortColumnNullOrEmpty(String sortColumn) {
        // given
        final int size = 10;
        final int pageNo = 0;
        final Sort.Direction direction = Sort.Direction.ASC;
        final PageRequest expectedPageReq = PageRequest.of(pageNo, size);

        // when
        final PageRequest actualPageReq = PageReqUtils.createPageReq(pageNo, size, direction, sortColumn);

        // then
        Assertions.assertEquals(expectedPageReq, actualPageReq);
    }

}