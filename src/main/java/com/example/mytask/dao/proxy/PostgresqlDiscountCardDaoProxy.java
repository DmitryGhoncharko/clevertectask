package com.example.mytask.dao.proxy;

import com.example.mytask.cache.Cache;
import com.example.mytask.dao.DiscountCardDao;
import com.example.mytask.exception.DaoException;
import com.example.mytask.model.DiscountCard;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class PostgresqlDiscountCardDaoProxy implements DiscountCardDao {
    private final DiscountCardDao discountCardDao;
    private final Cache<Long, DiscountCard> cache;

    @Override
    public Optional<DiscountCard> getCardById(String id) throws DaoException {
        if (id == null) {
            return Optional.empty();
        }
        DiscountCard discountCard = cache.get(Long.parseLong(id));
        if (discountCard != null) {
            return Optional.of(discountCard);
        } else {
            Optional<DiscountCard> discountCardOptional = discountCardDao.getCardById(id);
            discountCardOptional.ifPresent(card -> cache.put(Long.parseLong(id), card));
            return discountCardOptional;
        }
    }
}
