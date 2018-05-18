package de.metas.purchasecandidate.grossprofit;

import static java.math.BigDecimal.ONE;

import java.time.LocalDate;

import org.adempiere.bpartner.BPartnerId;
import org.adempiere.util.Services;
import org.compiere.model.I_C_Currency;
import org.compiere.util.Env;

import de.metas.currency.ICurrencyDAO;
import de.metas.money.Currency;
import de.metas.money.CurrencyId;
import de.metas.money.Money;
import de.metas.money.grossprofit.GrossProfitComputeRequest;
import de.metas.product.ProductId;
import de.metas.purchasecandidate.PurchaseCandidate;
import lombok.NonNull;

/*
 * #%L
 * de.metas.purchasecandidate.base
 * %%
 * Copyright (C) 2018 metas GmbH
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program. If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

public class GrossProfitComputeRequestCreator
{
	public static GrossProfitComputeRequest of(@NonNull final PurchaseCandidate purchaseCandidate)
	{
		final BPartnerId bPartnerId = BPartnerId.ofRepoId(purchaseCandidate.getVendorBPartnerId());
		final ProductId productId = ProductId.ofRepoId(purchaseCandidate.getProductId());
		final LocalDate date = purchaseCandidate.getDateRequired().toLocalDate();

		// TODO: use the purchase candidate's pricing info, or compute it right here if must be
		final I_C_Currency currencyrecordEUR = Services.get(ICurrencyDAO.class).retrieveCurrencyByISOCode(Env.getCtx(), "EUR");
		final Currency currency = Currency
				.builder()
				.id(CurrencyId.ofRepoId(currencyrecordEUR.getC_Currency_ID()))
				.precision(currencyrecordEUR.getStdPrecision())
				.build();

		return new GrossProfitComputeRequest(bPartnerId, productId, date, Money.of(ONE, currency));
	}
}
