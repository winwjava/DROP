
package org.drip.service.product;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2019 Lakshmi Krishnamurthy
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * Copyright (C) 2017 Lakshmi Krishnamurthy
 * Copyright (C) 2016 Lakshmi Krishnamurthy
 * 
 *  This file is part of DROP, an open-source library targeting risk, transaction costs, exposure, margin
 *  	calculations, and portfolio construction within and across fixed income, credit, commodity, equity,
 *  	FX, and structured products.
 *  
 *  	https://lakshmidrip.github.io/DROP/
 *  
 *  DROP is composed of three main modules:
 *  
 *  - DROP Analytics Core - https://lakshmidrip.github.io/DROP-Analytics-Core/
 *  - DROP Portfolio Core - https://lakshmidrip.github.io/DROP-Portfolio-Core/
 *  - DROP Numerical Core - https://lakshmidrip.github.io/DROP-Numerical-Core/
 * 
 * 	DROP Analytics Core implements libraries for the following:
 * 	- Fixed Income Analytics
 * 	- Asset Backed Analytics
 * 	- XVA Analytics
 * 	- Exposure and Margin Analytics
 * 
 * 	DROP Portfolio Core implements libraries for the following:
 * 	- Asset Allocation Analytics
 * 	- Transaction Cost Analytics
 * 
 * 	DROP Numerical Core implements libraries for the following:
 * 	- Statistical Learning Library
 * 	- Numerical Optimizer Library
 * 	- Machine Learning Library
 * 	- Spline Builder Library
 * 
 * 	Documentation for DROP is Spread Over:
 * 
 * 	- Main                     => https://lakshmidrip.github.io/DROP/
 * 	- Wiki                     => https://github.com/lakshmiDRIP/DROP/wiki
 * 	- GitHub                   => https://github.com/lakshmiDRIP/DROP
 * 	- Javadoc                  => https://lakshmidrip.github.io/DROP/Javadoc/index.html
 * 	- Technical Specifications => https://github.com/lakshmiDRIP/DROP/tree/master/Docs/Internal
 * 	- Release Versions         => https://lakshmidrip.github.io/DROP/version.html
 * 	- Community Credits        => https://lakshmidrip.github.io/DROP/credits.html
 * 	- Issues Catalog           => https://github.com/lakshmiDRIP/DROP/issues
 * 	- JUnit                    => https://lakshmidrip.github.io/DROP/junit/index.html
 * 	- Jacoco                   => https://lakshmidrip.github.io/DROP/jacoco/index.html
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *   	you may not use this file except in compliance with the License.
 *   
 *  You may obtain a copy of the License at
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  	distributed under the License is distributed on an "AS IS" BASIS,
 *  	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  
 *  See the License for the specific language governing permissions and
 *  	limitations under the License.
 */

/**
 * <i>FixFloatAPI</i> contains the Functionality associated with the Horizon Analysis of the Fix Float Swap.
 * 
 * <br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/NumericalCore.md">Numerical Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/AlgorithmSupportLibrary.md">Algorithm Support Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/service">Service</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/service/product">Product API</a></li>
 *  </ul>
 * <br><br>
 * 
 * @author Lakshmi Krishnamurthy
 */

public class FixFloatAPI {

	/**
	 * Compute the Horizon Change Attribution Details for the Specified Fix-Float Swap
	 * 
	 * @param dcFirst First Day Discount Curve
	 * @param dcSecond Second Date Discount Curve
	 * @param mapRollDownDiscountCurve Map of the Roll Down Discount Curve
	 * @param strMaturityTenor Fix Float Swap Maturity Tenor
	 * 
	 * @return The Horizon Change Attribution Instance
	 */

	public static final org.drip.historical.attribution.PositionChangeComponents HorizonChangeAttribution (
		final org.drip.state.discount.MergedDiscountForwardCurve dcFirst,
		final org.drip.state.discount.MergedDiscountForwardCurve dcSecond,
		final
			org.drip.analytics.support.CaseInsensitiveHashMap<org.drip.state.discount.MergedDiscountForwardCurve>
			mapRollDownDiscountCurve,
		final java.lang.String strMaturityTenor)
	{
		if (null == mapRollDownDiscountCurve || 0 == mapRollDownDiscountCurve.size()) return null;

		org.drip.market.otc.FixedFloatSwapConvention ffsc =
			org.drip.market.otc.IBORFixedFloatContainer.ConventionFromJurisdiction (dcFirst.currency(),
				"ALL", strMaturityTenor, "MAIN");

		if (null == ffsc) return null;

		int iSettleLag = ffsc.spotLag();

		org.drip.analytics.date.JulianDate dtFirst = dcFirst.epoch();

		org.drip.product.rates.FixFloatComponent ffc = ffsc.createFixFloatComponent (dtFirst,
			strMaturityTenor, 0., 0., 1.);

		if (null == ffc) return null;

		org.drip.param.market.CurveSurfaceQuoteContainer csqcFirst = new
			org.drip.param.market.CurveSurfaceQuoteContainer();

		if (!csqcFirst.setFundingState (dcFirst)) return null;

		org.drip.param.market.CurveSurfaceQuoteContainer csqcSecond = new
			org.drip.param.market.CurveSurfaceQuoteContainer();

		if (!csqcSecond.setFundingState (dcSecond)) return null;

		org.drip.analytics.support.CaseInsensitiveHashMap<org.drip.param.market.CurveSurfaceQuoteContainer>
			mapCSQCRollDown = new
				org.drip.analytics.support.CaseInsensitiveHashMap<org.drip.param.market.CurveSurfaceQuoteContainer>();

		for (java.lang.String strRollDownTenor : mapRollDownDiscountCurve.keySet()) {
			org.drip.param.market.CurveSurfaceQuoteContainer csqcRollDown = new
				org.drip.param.market.CurveSurfaceQuoteContainer();

			org.drip.state.discount.MergedDiscountForwardCurve dcRollDown = mapRollDownDiscountCurve.get
				(strRollDownTenor);

			if (null == dcRollDown || !csqcRollDown.setFundingState (dcRollDown)) return null;

			mapCSQCRollDown.put (strRollDownTenor, csqcRollDown);
		}

		try {
			double dblSwapRate = ffc.measureValue (org.drip.param.valuation.ValuationParams.Spot
				(dtFirst.addBusDays (iSettleLag, ffc.payCurrency()).julian()), null, csqcFirst, null,
					"SwapRate");

			return org.drip.historical.engine.HorizonChangeExplainExecutor.GenerateAttribution (new
				org.drip.historical.engine.FixFloatExplainProcessor (ffsc.createFixFloatComponent (dtFirst,
					strMaturityTenor, dblSwapRate, 0., 1.), iSettleLag, "SwapRate", dblSwapRate, dtFirst,
						dcSecond.epoch(), csqcFirst, csqcSecond, mapCSQCRollDown));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Generate the Funding Curve Horizon Metrics
	 * 
	 * @param dtFirst The First Date
	 * @param dtSecond The Second Date
	 * @param astrFundingDepositInstrumentTenor Array of Funding Curve Deposit Instrument Maturity Tenors
	 * @param adblFirstFundingDepositInstrument Array of First Date Funding Curve Deposit Instrument Quotes
	 * @param adblSecondFundingDepositInstrument Array of Second Date Funding Curve Deposit Instrument Quotes
	 * @param astrFundingFixFloatTenor Array of Funding Curve Fix Float Instrument Maturity Tenors
	 * @param adblFirstFundingFixFloat Array of First Date Funding Curve Fix Float Swap Rates
	 * @param adblSecondFundingFixFloat Array of Second Date Funding Curve Fix Float Swap Rates
	 * @param strCurrency Funding Currency
	 * @param strMaturityTenor Maturity Tenor
	 * @param astrRollDownHorizon Array of the Roll Down Horizon Tenors
	 * @param iLatentStateType Latent State Type
	 * 
	 * @return The Funding Curve Horizon Metrics
	 */

	public static final org.drip.historical.attribution.PositionChangeComponents HorizonChangeAttribution (
		final org.drip.analytics.date.JulianDate dtFirst,
		final org.drip.analytics.date.JulianDate dtSecond,
		final java.lang.String[] astrFundingDepositInstrumentTenor,
		final double[] adblFirstFundingDepositInstrument,
		final double[] adblSecondFundingDepositInstrument,
		final java.lang.String[] astrFundingFixFloatTenor,
		final double[] adblFirstFundingFixFloat,
		final double[] adblSecondFundingFixFloat,
		final java.lang.String strCurrency,
		final java.lang.String strMaturityTenor,
		final java.lang.String[] astrRollDownHorizon,
		final int iLatentStateType)
	{
		if (null == dtFirst || null == dtSecond || dtFirst.julian() >= dtSecond.julian()) return null;

		int iNumFundingDepositInstrument = null == astrFundingDepositInstrumentTenor ? 0 :
			astrFundingDepositInstrumentTenor.length;
		int iNumFirstFundingDepositInstrument = null == adblFirstFundingDepositInstrument ? 0 :
			adblFirstFundingDepositInstrument.length;
		int iNumSecondFundingDepositInstrument = null == adblSecondFundingDepositInstrument ? 0 :
			adblSecondFundingDepositInstrument.length;
		int iNumFundingFixFloat = null == astrFundingFixFloatTenor ? 0 : astrFundingFixFloatTenor.length;
		int iNumFirstFundingFixFloat = null == adblFirstFundingFixFloat ? 0 :
			adblFirstFundingFixFloat.length;
		int iNumSecondFundingFixFloat = null == adblSecondFundingFixFloat ? 0 :
			adblSecondFundingFixFloat.length;
		int iNumRollDownHorizon = null == astrRollDownHorizon ? 0 : astrRollDownHorizon .length;

		org.drip.analytics.support.CaseInsensitiveHashMap<org.drip.state.discount.MergedDiscountForwardCurve>
			mapRollDownDiscountCurve = 0 == iNumRollDownHorizon ? null : new
				org.drip.analytics.support.CaseInsensitiveHashMap<org.drip.state.discount.MergedDiscountForwardCurve>();

		if (0 == iNumFundingDepositInstrument || iNumFundingDepositInstrument !=
			iNumFirstFundingDepositInstrument || iNumFundingDepositInstrument !=
				iNumSecondFundingDepositInstrument || 0 == iNumFundingFixFloat || iNumFundingFixFloat !=
					iNumFirstFundingFixFloat || iNumFundingFixFloat != iNumSecondFundingFixFloat)
			return null;

		org.drip.state.discount.MergedDiscountForwardCurve dcFirst =
			org.drip.service.template.LatentMarketStateBuilder.FundingCurve (dtFirst, strCurrency,
				astrFundingDepositInstrumentTenor, adblFirstFundingDepositInstrument, "ForwardRate", null,
					"ForwardRate", astrFundingFixFloatTenor, adblFirstFundingFixFloat, "SwapRate",
						iLatentStateType);

		org.drip.state.discount.MergedDiscountForwardCurve dcSecond =
			org.drip.service.template.LatentMarketStateBuilder.FundingCurve (dtSecond, strCurrency,
				astrFundingDepositInstrumentTenor, adblSecondFundingDepositInstrument, "ForwardRate", null,
					"ForwardRate", astrFundingFixFloatTenor, adblSecondFundingFixFloat, "SwapRate",
						iLatentStateType);

		org.drip.state.discount.MergedDiscountForwardCurve dcRollDown =
			org.drip.service.template.LatentMarketStateBuilder.FundingCurve (dtSecond, strCurrency,
				astrFundingDepositInstrumentTenor, adblFirstFundingDepositInstrument, "ForwardRate", null,
					"ForwardRate", astrFundingFixFloatTenor, adblFirstFundingFixFloat, "SwapRate",
						iLatentStateType);

		if (null == dcRollDown) return null;

		mapRollDownDiscountCurve.put ("Native", dcRollDown);

		for (int j = 0; j < iNumRollDownHorizon; ++j) {
			org.drip.state.discount.MergedDiscountForwardCurve dcHorizonRollDown =
				org.drip.service.template.LatentMarketStateBuilder.FundingCurve (dtFirst.addTenor
					(astrRollDownHorizon[j]), strCurrency, astrFundingDepositInstrumentTenor,
						adblFirstFundingDepositInstrument, "ForwardRate", null, "ForwardRate",
							astrFundingFixFloatTenor, adblFirstFundingFixFloat, "SwapRate",
								iLatentStateType);

			if (null == dcHorizonRollDown) return null;

			mapRollDownDiscountCurve.put (astrRollDownHorizon[j], dcHorizonRollDown);
		}

		return HorizonChangeAttribution (dcFirst, dcSecond, mapRollDownDiscountCurve, strMaturityTenor);
	}

	/**
	 * Generate the Funding Curve Horizon Metrics
	 * 
	 * @param adtSpot Array of Spot
	 * @param iHorizonGap The Horizon Gap
	 * @param astrFundingDepositInstrumentTenor Array of Funding Curve Deposit Instrument Maturity Tenors
	 * @param aadblFundingDepositInstrumentQuote Array of Funding Curve Deposit Instrument Forward Rates
	 * @param astrFundingFixFloatTenor Array of Funding Curve Fix Float Instrument Maturity Tenors
	 * @param aadblFundingFixFloatQuote Array of Funding Curve Fix Float Instrument Swap Rates
	 * @param strCurrency Funding Currency
	 * @param strMaturityTenor Maturity Tenor
	 * @param astrRollDownHorizon Array of the Roll Down Horizon Tenors
	 * @param iLatentStateType Latent State Type
	 * 
	 * @return The Funding Curve Horizon Metrics
	 */

	public static final java.util.List<org.drip.historical.attribution.PositionChangeComponents>
		HorizonChangeAttribution (
			final org.drip.analytics.date.JulianDate[] adtSpot,
			final int iHorizonGap,
			final java.lang.String[] astrFundingDepositInstrumentTenor,
			final double[][] aadblFundingDepositInstrumentQuote,
			final java.lang.String[] astrFundingFixFloatTenor,
			final double[][] aadblFundingFixFloatQuote,
			final java.lang.String strCurrency,
			final java.lang.String strMaturityTenor,
			final java.lang.String[] astrRollDownHorizon,
			final int iLatentStateType)
	{
		if (null == adtSpot || 0 >= iHorizonGap || null == aadblFundingDepositInstrumentQuote || null ==
			aadblFundingFixFloatQuote)
			return null;

		int iNumClose = adtSpot.length;
		int iNumRollDownTenor = null == astrRollDownHorizon ? 0 : astrRollDownHorizon.length;

		if (0 == iNumClose || iNumClose != aadblFundingDepositInstrumentQuote.length || iNumClose !=
			aadblFundingFixFloatQuote.length || 0 == iNumRollDownTenor)
			return null;

		java.util.List<org.drip.historical.attribution.PositionChangeComponents> lsPCC = new
			java.util.ArrayList<org.drip.historical.attribution.PositionChangeComponents>();

		for (int i = iHorizonGap; i < iNumClose; ++i) {
			org.drip.historical.attribution.PositionChangeComponents pcc = HorizonChangeAttribution
				(adtSpot[i - iHorizonGap], adtSpot[i], astrFundingDepositInstrumentTenor,
					aadblFundingDepositInstrumentQuote[i - iHorizonGap],
						aadblFundingDepositInstrumentQuote[i], astrFundingFixFloatTenor,
							aadblFundingFixFloatQuote[i - iHorizonGap], aadblFundingFixFloatQuote[i],
								strCurrency, strMaturityTenor, astrRollDownHorizon, iLatentStateType);

			if (null != pcc) lsPCC.add (pcc);
		}

		return lsPCC;
	}
}
