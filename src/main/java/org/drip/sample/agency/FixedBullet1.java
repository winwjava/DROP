
package org.drip.sample.agency;

import java.util.Map;

import org.drip.analytics.date.*;
import org.drip.analytics.output.BondRVMeasures;
import org.drip.analytics.support.Helper;
import org.drip.numerical.common.FormatUtil;
import org.drip.param.creator.MarketParamsBuilder;
import org.drip.param.market.CurveSurfaceQuoteContainer;
import org.drip.param.quote.*;
import org.drip.param.valuation.*;
import org.drip.product.creator.BondBuilder;
import org.drip.product.credit.BondComponent;
import org.drip.product.definition.*;
import org.drip.service.env.EnvManager;
import org.drip.service.template.*;
import org.drip.state.discount.MergedDiscountForwardCurve;
import org.drip.state.govvie.GovvieCurve;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2019 Lakshmi Krishnamurthy
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * Copyright (C) 2017 Lakshmi Krishnamurthy
 * 
 *  This file is part of DROP, an open-source library targeting risk, transaction costs, exposure, margin
 *  	calculations, valuation adjustment, and portfolio construction within and across fixed income,
 *  	credit, commodity, equity, FX, and structured products.
 *  
 *  	https://lakshmidrip.github.io/DROP/
 *  
 *  DROP is composed of three modules:
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
 * 	- Statistical Learning
 * 	- Numerical Optimizer
 * 	- Spline Builder
 * 	- Algorithm Support
 * 
 * 	Documentation for DROP is Spread Over:
 * 
 * 	- Main                     => https://lakshmidrip.github.io/DROP/
 * 	- Wiki                     => https://github.com/lakshmiDRIP/DROP/wiki
 * 	- GitHub                   => https://github.com/lakshmiDRIP/DROP
 * 	- Repo Layout Taxonomy     => https://github.com/lakshmiDRIP/DROP/blob/master/Taxonomy.md
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
 * <i>FixedBullet1</i> demonstrates Non-EOS Fixed Coupon Agency Bond Pricing and Relative Value Measure
 * Generation Functionality.
 * 
 * <br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/AnalyticsCore.md">Analytics Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/FixedIncomeAnalyticsLibrary.md">Fixed Income Analytics Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/sample/README.md">Sample</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/sample/agency/README.md">Agency Bonds</a></li>
 *  </ul>
 * <br><br>
 * 
 * @author Lakshmi Krishnamurthy
 */

public class FixedBullet1 {

	private static final MergedDiscountForwardCurve FundingCurve (
		final JulianDate dtSpot,
		final String strCurrency)
		throws Exception
	{
		String[] astrDepositMaturityTenor = new String[] {
			"2D"
		};

		double[] adblDepositQuote = new double[] {
			0.0111956 // 2D
		};

		double[] adblFuturesQuote = new double[] {
			0.011375,	// 98.8625
			0.013350,	// 98.6650
			0.014800,	// 98.5200
			0.016450,	// 98.3550
			0.017850,	// 98.2150
			0.019300	// 98.0700
		};

		String[] astrFixFloatMaturityTenor = new String[] {
			"02Y",
			"03Y",
			"04Y",
			"05Y",
			"06Y",
			"07Y",
			"08Y",
			"09Y",
			"10Y",
			"11Y",
			"12Y",
			"15Y",
			"20Y",
			"25Y",
			"30Y",
			"40Y",
			"50Y"
		};

		double[] adblFixFloatQuote = new double[] {
			0.017029, //  2Y
			0.019354, //  3Y
			0.021044, //  4Y
			0.022291, //  5Y
			0.023240, //  6Y
			0.024025, //  7Y
			0.024683, //  8Y
			0.025243, //  9Y
			0.025720, // 10Y
			0.026130, // 11Y
			0.026495, // 12Y
			0.027230, // 15Y
			0.027855, // 20Y
			0.028025, // 25Y
			0.028028, // 30Y
			0.027902, // 40Y
			0.027655  // 50Y
		};

		MergedDiscountForwardCurve dcFunding = LatentMarketStateBuilder.SmoothFundingCurve (
			dtSpot,
			strCurrency,
			astrDepositMaturityTenor,
			adblDepositQuote,
			"ForwardRate",
			adblFuturesQuote,
			"ForwardRate",
			astrFixFloatMaturityTenor,
			adblFixFloatQuote,
			"SwapRate"
		);

		Component[] aDepositComp = OTCInstrumentBuilder.FundingDeposit (
			dtSpot,
			strCurrency,
			astrDepositMaturityTenor
		);

		Component[] aFuturesComp = ExchangeInstrumentBuilder.ForwardRateFuturesPack (
			dtSpot,
			adblFuturesQuote.length,
			strCurrency
		);

		Component[] aFixFloatComp = OTCInstrumentBuilder.FixFloatStandard (
			dtSpot,
			strCurrency,
			"ALL",
			astrFixFloatMaturityTenor,
			"MAIN",
			0.
		);

		ValuationParams valParams = new ValuationParams (
			dtSpot,
			dtSpot,
			strCurrency
		);

		CurveSurfaceQuoteContainer csqc = MarketParamsBuilder.Create (
			dcFunding,
			null,
			null,
			null,
			null,
			null,
			null
		);

		System.out.println();

		System.out.println ("\t|-------------------------------------||");

		System.out.println ("\t|        DEPOSIT INPUT vs. CALC       ||");

		System.out.println ("\t|-------------------------------------||");

		for (int i = 0; i < aDepositComp.length; ++i)
			System.out.println ("\t| [" + aDepositComp[i].maturityDate() + "] =" +
				FormatUtil.FormatDouble (aDepositComp[i].measureValue (
					valParams,
					null,
					csqc,
					null,
					"ForwardRate"
				), 1, 6, 1.) + " |" +
				FormatUtil.FormatDouble (adblDepositQuote[i], 1, 6, 1.) + " ||"
			);

		System.out.println ("\t|-------------------------------------||");

		System.out.println();

		System.out.println ("\t|-------------------------------------||");

		System.out.println ("\t|        FUTURES INPUT vs. CALC       ||");

		System.out.println ("\t|-------------------------------------||");

		for (int i = 0; i < aFuturesComp.length; ++i)
			System.out.println ("\t| [" + aFuturesComp[i].maturityDate() + "] =" +
				FormatUtil.FormatDouble (aFuturesComp[i].measureValue (
					valParams,
					null,
					csqc,
					null,
					"ForwardRate"
				), 1, 6, 1.) + " |" +
				FormatUtil.FormatDouble (adblFuturesQuote[i], 1, 6, 1.) + " ||"
			);

		System.out.println ("\t|-------------------------------------||");

		System.out.println();

		System.out.println ("\t|------------------------------------------------|| ");

		System.out.println ("\t|          FIX-FLOAT INPUTS vs CALIB             ||");

		System.out.println ("\t|------------------------------------------------|| ");

		for (int i = 0; i < aFixFloatComp.length; ++i)
			System.out.println ("\t| [" + aFixFloatComp[i].maturityDate() + "] =" +
				FormatUtil.FormatDouble (aFixFloatComp[i].measureValue (
					valParams,
					null,
					csqc,
					null,
					"CalibSwapRate"
				), 1, 6, 1.) + " |" +
				FormatUtil.FormatDouble (adblFixFloatQuote[i], 1, 6, 1.) + " |" +
				FormatUtil.FormatDouble (aFixFloatComp[i].measureValue (
					valParams,
					null,
					csqc,
					null,
					"FairPremium"
				), 1, 6, 1.) + " ||"
			);

		System.out.println ("\t|------------------------------------------------||");

		System.out.println();

		return dcFunding;
	}

	private static final Map<String, GovvieCurve> GovvieCurve (
		final JulianDate dtSpot,
		final String strCode,
		final double[] adblCoupon,
		final double[] adblYield)
		throws Exception
	{
		JulianDate[] adtEffective = new JulianDate[] {
			dtSpot,
			dtSpot,
			dtSpot,
			dtSpot,
			dtSpot,
			dtSpot,
			dtSpot,
			dtSpot
		};

		JulianDate[] adtMaturity = new JulianDate[] {
			dtSpot.addTenor ("1Y"),
			dtSpot.addTenor ("2Y"),
			dtSpot.addTenor ("3Y"),
			dtSpot.addTenor ("5Y"),
			dtSpot.addTenor ("7Y"),
			dtSpot.addTenor ("10Y"),
			dtSpot.addTenor ("20Y"),
			dtSpot.addTenor ("30Y")
		};

		Map<String, GovvieCurve> mapGovvieCurve = LatentMarketStateBuilder.BumpedGovvieCurve (
			strCode,
			dtSpot,
			adtEffective,
			adtMaturity,
			adblCoupon,
			adblYield,
			"Yield",
			LatentMarketStateBuilder.SHAPE_PRESERVING,
			0.0001,
			false
		);

		BondComponent[] aComp = TreasuryBuilder.FromCode (
			strCode,
			adtEffective,
			adtMaturity,
			adblCoupon
		);

		ValuationParams valParams = ValuationParams.Spot (dtSpot.julian());

		CurveSurfaceQuoteContainer csqc = new CurveSurfaceQuoteContainer();

		csqc.setGovvieState (mapGovvieCurve.get ("BASE"));

		System.out.println();

		System.out.println ("\t|-------------------------------------------||");

		System.out.println ("\t|       TREASURY INPUT vs CALIB YIELD       ||");

		System.out.println ("\t|-------------------------------------------||");

		for (int i = 0; i < aComp.length; ++i)
			System.out.println ("\t| " + aComp[i].name() + " | " +
				FormatUtil.FormatDouble (adblYield[i], 1, 3, 100.) + "% | " +
				FormatUtil.FormatDouble (aComp[i].yieldFromPrice (
					valParams,
					null,
					null,
					aComp[i].maturityDate().julian(),
					1.,
					aComp[i].priceFromYield (
						valParams,
						null,
						null,
						mapGovvieCurve.get ("BASE").yield (aComp[i].maturityDate().julian())
					)
				), 1, 3, 100.) + "% ||"
			);

		System.out.println ("\t|-------------------------------------------||");

		return mapGovvieCurve;
	}

	private static final void AccumulateBondMarketQuote (
		final CurveSurfaceQuoteContainer csqc,
		final String[] astrOnTheRunCode,
		final double[] adblYield)
		throws Exception
	{
		for (int i = 0; i < astrOnTheRunCode.length; ++i) {
			ProductMultiMeasure pmmq = new ProductMultiMeasure();

			pmmq.addQuote (
				"Yield",
				new MultiSided (
					"mid",
					adblYield[i]
				),
				true
			);

			csqc.setProductQuote (
				astrOnTheRunCode[i],
				pmmq
			);
		}
	}

	private static final Bond Corporate (
		final String strName,
		final JulianDate dtEffective,
		final JulianDate dtMaturity,
		final double dblCoupon,
		final int iFreq,
		final String strDayCount)
		throws Exception
	{
		return BondBuilder.CreateSimpleFixed (
			strName + FormatUtil.FormatDouble (dblCoupon, 1, 4, 100.) + " " + dtMaturity,
			"USD",
			"",
			dblCoupon,
			iFreq,
			strDayCount,
			dtEffective,
			dtMaturity,
			null,
			null
		);
	}

	private static final double[] RVMeasures (
		final Bond[] aBond,
		final JulianDate dtValue,
		final CurveSurfaceQuoteContainer csqc,
		final double[] adblCleanPrice)
		throws Exception
	{
		JulianDate dtSettle = dtValue.addBusDays (
			0,
			aBond[0].currency()
		);

		ValuationParams valParams = new ValuationParams (
			dtValue,
			dtSettle,
			aBond[0].currency()
		);

		System.out.println();

		System.out.println ("\t|--------------------------------||");

		System.out.println ("\t| Trade Date       : " + dtValue + " ||");

		System.out.println ("\t| Cash Settle Date : " + dtSettle + " ||");

		System.out.println ("\t|--------------------------------||");

		System.out.println();

		String strCurveMetrics = "";
		String strSecularMetrics = "";
		double[] adblOAS = new double[aBond.length];

		for (int i = 0; i < aBond.length; ++i) {
			System.out.println ("Doing " + aBond[i].name());

			WorkoutInfo wi = aBond[i].exerciseYieldFromPrice (
				valParams,
				csqc,
				null,
				adblCleanPrice[i]
			);

			BondRVMeasures rvm = aBond[i].standardMeasures (
				valParams,
				null,
				csqc,
				null,
				wi,
				adblCleanPrice[i]
			);

			strSecularMetrics += "\t| " +
				aBond[i].name() + " | " +
				aBond[i].effectiveDate() + " | " +
				aBond[i].maturityDate() + " |  " +
				aBond[i].firstCouponDate() + "  |" +
				FormatUtil.FormatDouble (adblCleanPrice[i], 3, 3, 100.) + " |" +
				FormatUtil.FormatDouble (aBond[i].accrued (dtSettle.julian(), csqc), 1, 5, 100.) + " |" +
				FormatUtil.FormatDouble (wi.yield(), 1, 2, 100.) + "% | " +
				FormatUtil.FormatDouble (rvm.macaulayDuration(), 2, 2, 1.) + "  | " +
				FormatUtil.FormatDouble (rvm.modifiedDuration(), 2, 2, 10000.) + "  |  " +
				FormatUtil.FormatDouble (rvm.yield01(), 2, 2, 10000.) + "  |" +
				FormatUtil.FormatDouble (rvm.yield01(), 4, 0, 1000000.) + " |" +
				FormatUtil.FormatDouble (rvm.convexity(), 1, 2, 1000000.) + " |" +
				FormatUtil.FormatDouble (aBond[i].weightedAverageLife (valParams, csqc), 2, 2, 1.) + " |   " +
				FormatUtil.FormatDouble (rvm.bondBasis(), 3, 0, 10000.) + "     ||" + "\n";

			adblOAS[i] = rvm.oas();

			double dblCleanPriceOASUp = aBond[i].priceFromOAS (
				valParams,
				csqc,
				null,
				adblOAS[i] + 0.0001
			);

			double dblCleanPriceOASDown = aBond[i].priceFromOAS (
				valParams,
				csqc,
				null,
				adblOAS[i] - 0.0001
			);

			strCurveMetrics += "\t| " +
				aBond[i].name() + " |" +
				FormatUtil.FormatDouble (adblCleanPrice[i], 3, 3, 100.) + " |" +
				FormatUtil.FormatDouble (wi.yield(), 1, 2, 100.) + "% |   " +
				FormatUtil.FormatDouble (rvm.zSpread(), 3, 0, 10000.) + "   |" +
				FormatUtil.FormatDouble (adblOAS[i], 3, 0, 10000.) + " | " +
				FormatUtil.FormatDouble (0.5 * (dblCleanPriceOASDown - dblCleanPriceOASUp) / adblCleanPrice[i], 2, 2, 10000.) + "  |  " +
				FormatUtil.FormatDouble ((dblCleanPriceOASDown + dblCleanPriceOASUp - 2. * adblCleanPrice[i]) / adblCleanPrice[i], 2, 2, 1000000.) + "   |" +
				FormatUtil.FormatDouble (rvm.asw(), 3, 0, 10000.) + " |  " +
				FormatUtil.FormatDouble (rvm.gSpread(), 3, 0, 10000.) + "    |   " +
				FormatUtil.FormatDouble (rvm.iSpread(), 3, 0, 10000.) + "   |    " +
				FormatUtil.FormatDouble (rvm.tsySpread(), 3, 0, 10000.) + "    |  " +
				Helper.BaseTsyBmk (
					dtValue.julian(),
					aBond[i].maturityDate().julian()
				) + "  ||" + "\n";
		}

		System.out.println ("\t|----------------------------------------------------------------------------------------------------------------------------------------------------------------------||");

		System.out.println ("\t|             BOND           |  EFFECTIVE  |   MATURITY  |  FIRST COUPON |  PRICE  | ACCRUED | YIELD | MAC DUR | MOD DUR | YIELD 01 | DV01 | CONV |  WAL  | BOND BASIS ||");

		System.out.println ("\t|----------------------------------------------------------------------------------------------------------------------------------------------------------------------||");

		System.out.print (strSecularMetrics);

		System.out.println ("\t|----------------------------------------------------------------------------------------------------------------------------------------------------------------------||");

		System.out.println ("\t|----------------------------------------------------------------------------------------------------------------------------------------||");

		System.out.println ("\t|             BOND           |  PRICE  | YIELD | Z SPREAD | OAS | OAS DUR |  OAS CONV | ASW | G SPREAD | I SPREAD | TSY SPREAD | TSY BMK ||");

		System.out.println ("\t|----------------------------------------------------------------------------------------------------------------------------------------||");

		System.out.print (strCurveMetrics);

		System.out.println ("\t|----------------------------------------------------------------------------------------------------------------------------------------||");

		return adblOAS;
	}

	public static final void main (
		final String[] astrArgs)
		throws Exception
	{
		EnvManager.InitEnv (
			"",
			true
		);

		JulianDate dtSpot = DateUtil.CreateFromYMD (
			2017,
			DateUtil.MARCH,
			13
		);

		String strCurrency = "USD";
		String strTreasuryCode = "UST";

		MergedDiscountForwardCurve dcFunding = FundingCurve (
			dtSpot,
			strCurrency
		);

		double[] adblTreasuryCoupon = new double[] {
			0.0100,
			0.0100,
			0.0125,
			0.0150,
			0.0200,
			0.0225,
			0.0250,
			0.0300
		};

		double[] adblTreasuryYield = new double[] {
			0.0104,	//  1Y
			0.0137, //  2Y
			0.0167, //  3Y
			0.0213, //  5Y
			0.0243, //  7Y
			0.0260, // 10Y
			0.0294, // 20Y
			0.0319  // 30Y
		};

		Map<String, GovvieCurve> mapGovvieCurve = GovvieCurve (
			dtSpot,
			strTreasuryCode,
			adblTreasuryCoupon,
			adblTreasuryYield
		);

		CurveSurfaceQuoteContainer csqc = MarketParamsBuilder.Create (
			dcFunding,
			null,
			null,
			null,
			null,
			null,
			null
		);

		csqc.setGovvieState (mapGovvieCurve.get ("BASE"));

		AccumulateBondMarketQuote (
			csqc,
			new String[] {
				"01YON",
				"02YON",
				"03YON",
				"05YON",
				"07YON",
				"10YON",
				"20YON",
				"30YON"
			},
			adblTreasuryYield
		);

		Bond[] aCorporateBond = new Bond[] {
			Corporate ("AGENCY ", DateUtil.CreateFromYMD (2013, 10, 10), DateUtil.CreateFromYMD (2028,  4, 10), 0.04000, 2, "30/360"),
			Corporate ("AGENCY ", DateUtil.CreateFromYMD (2013, 10,  4), DateUtil.CreateFromYMD (2028,  9,  1), 0.04000, 2, "30/360"),
			Corporate ("AGENCY ", DateUtil.CreateFromYMD (2016,  2,  2), DateUtil.CreateFromYMD (2035,  2,  2), 0.03300, 2, "30/360"),
			Corporate ("AGENCY ", DateUtil.CreateFromYMD (2016,  6,  9), DateUtil.CreateFromYMD (2036,  5, 20), 0.03040, 2, "30/360"),
			Corporate ("AGENCY ", DateUtil.CreateFromYMD (2006,  7, 17), DateUtil.CreateFromYMD (2036,  7, 15), 0.05500, 2, "30/360"),
			Corporate ("AGENCY ", DateUtil.CreateFromYMD (2007,  7, 18), DateUtil.CreateFromYMD (2037,  7, 15), 0.05625, 2, "30/360"),
			Corporate ("AGENCY ", DateUtil.CreateFromYMD (2016, 11,  2), DateUtil.CreateFromYMD (2037, 11,  2), 0.02780, 2, "30/360"),
			Corporate ("AGENCY ", DateUtil.CreateFromYMD (1998,  8,  6), DateUtil.CreateFromYMD (2038,  8,  6), 0.06210, 2, "30/360"),
			Corporate ("AGENCY ", DateUtil.CreateFromYMD (2016,  7, 26), DateUtil.CreateFromYMD (2038,  8, 26), 0.02710, 2, "30/360"),
			Corporate ("AGENCY ", DateUtil.CreateFromYMD (2016,  2,  2), DateUtil.CreateFromYMD (2039,  2,  2), 0.03500, 2, "30/360"),
			Corporate ("AGENCY ", DateUtil.CreateFromYMD (2009,  9, 22), DateUtil.CreateFromYMD (2039,  9, 15), 0.05250, 2, "30/360"),
		};

		double[] adblCleanPrice = new double[] {
			1.0822160,	// (2028,  4, 10)
			1.0751340,	// (2028,  9,  1)
			0.9566711,	// (2035,  2,  2)
			0.9161351,	// (2036,  5, 20)
			1.2964900,	// (2036,  7, 15)
			1.3208360,	// (2037,  7, 15)
			0.8635154,	// (2037, 11,  2)
			1.4179350,	// (2038,  8,  6)
			0.8431670,	// (2038,  8, 26)
			0.9582438,	// (2039,  2,  2)
			1.2435400,	// (2039,  9, 15)
		};

		double[] adblOAS = RVMeasures (
			aCorporateBond,
			dtSpot,
			csqc,
			adblCleanPrice
		);

		ValuationParams valParams = new ValuationParams (
			dtSpot,
			dtSpot.addBusDays (
				3,
				dcFunding.currency()
			),
			dcFunding.currency()
		);

		System.out.println();

		System.out.println ("\t|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------||");

		System.out.print ("\t|             BOND          ");

		for (Map.Entry<String, GovvieCurve> meGovvieCurve : mapGovvieCurve.entrySet()) {
			if ("BASE".equalsIgnoreCase (meGovvieCurve.getKey()) || "BUMP".equalsIgnoreCase (meGovvieCurve.getKey()))
				continue;

			System.out.print (" | " + meGovvieCurve.getKey());
		}

		System.out.println (" ||");

		System.out.println ("\t|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------||");

		for (int i = 0; i < adblOAS.length; ++i) {
			System.out.print ("\t| " + aCorporateBond[i].name());

			for (Map.Entry<String, GovvieCurve> meGovvieCurve : mapGovvieCurve.entrySet()) {
				if ("BASE".equalsIgnoreCase (meGovvieCurve.getKey()) || "BUMP".equalsIgnoreCase (meGovvieCurve.getKey()))
					continue;

				csqc.setGovvieState (meGovvieCurve.getValue());

				System.out.print (" |      " +
					FormatUtil.FormatDouble (
						(adblCleanPrice[i] - aCorporateBond[i].priceFromOAS (
							valParams,
							csqc,
							null,
							adblOAS[i]
						)) / adblCleanPrice[i],
					2, 2, 10000.) + "     "
				);
			}

			System.out.println (" ||");
		}

		System.out.println ("\t|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------||");

		EnvManager.TerminateEnv();
	}
}
