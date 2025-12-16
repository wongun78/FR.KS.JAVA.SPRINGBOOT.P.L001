package fpt.kiennt169.springboot.dtos;

import java.util.List;

import lombok.*;

/**
 * Generic paginated response DTO.
 *
 * @param <T> Type of content items
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponseDTO<T> {

    private List<T> content;

    private int pageNumber;

    private int pageSize;

    private long totalElements;

    private int totalPages;

    private boolean first;

    private boolean last;

    private boolean hasContent;

    private boolean hasNext;

    private boolean hasPrevious;

    /**
     * Creates a PageResponseDTO from Spring's Page object.
     */
    public static <T> PageResponseDTO<T> from(org.springframework.data.domain.Page<T> page) {
        return PageResponseDTO.<T>builder()
                .content(page.getContent())
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasContent(page.hasContent())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
}
