package ro.msg.learning.shop.util;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dto.SortOption;

import java.util.ArrayList;
import java.util.List;

@Component
public class RepositoryUtils {
    public void appendSortElements(StringBuilder query, Sort sort) {

        if (sort.isUnsorted()) {
            return;
        }

        query.append("ORDER BY ");

        sort.iterator().forEachRemaining(order -> query.append(order.getProperty()).append(" ")
                .append(order.getDirection()).append(","));

        query.replace(query.length() - 1, query.length(), " ");
    }

    public Sort generateSort(List<SortOption> sortOptions) {

        if (sortOptions.isEmpty()) return Sort.unsorted();

        List<Sort.Order> orders = new ArrayList<>();
        sortOptions.forEach(sortOption -> {
            if (sortOption.getDirection().equals("DESC") || sortOption.getDirection().equals("desc"))
                orders.add(Sort.Order.desc(sortOption.getColumn()));
            else
                orders.add(Sort.Order.asc(sortOption.getColumn()));
        });

        return Sort.by(orders);
    }
}
