
package org.drip.sample.treasuryfutures;

import java.util.Map;

import org.drip.analytics.date.*;
import org.drip.analytics.daycount.Convention;
import org.drip.analytics.output.BondRVMeasures;
import org.drip.numerical.common.FormatUtil;
import org.drip.param.creator.MarketParamsBuilder;
import org.drip.param.market.CurveSurfaceQuoteContainer;
import org.drip.param.quote.*;
import org.drip.param.valuation.*;
import org.drip.product.credit.*;
import org.drip.product.definition.*;
import org.drip.product.govvie.TreasuryFutures;
import org.drip.service.env.EnvManager;
import org.drip.service.template.*;
import org.drip.state.creator.ScenarioRepoCurveBuilder;
import org.drip.state.discount.MergedDiscountForwardCurve;
import org.drip.state.govvie.GovvieCurve;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * Copyright (C) 2017 Lakshmi Krishnamurthy
 * Copyright (C) 2016 Lakshmi Krishnamurthy
 * Copyright (C) 2015 Lakshmi Krishnamurthy
 * 
 *  This file is part of DRIP, a free-software/open-source library for buy/side financial/trading model
 *  	libraries targeting analysts and developers
 *  	https://lakshmidrip.github.io/DRIP/
 *  
 *  DRIP is composed of four main libraries:
 *  
 *  - DRIP Fixed Income - https://lakshmidrip.github.io/DRIP-Fixed-Income/
 *  - DRIP Asset Allocation - https://lakshmidrip.github.io/DRIP-Asset-Allocation/
 *  - DRIP Numerical Optimizer - https://lakshmidrip.github.io/DRIP-Numerical-Optimizer/
 *  - DRIP Statistical Learning - https://lakshmidrip.github.io/DRIP-Statistical-Learning/
 * 
 *  - DRIP Fixed Income: Library for Instrument/Trading Conventions, Treasury Futures/Options,
 *  	Funding/Forward/Overnight Curves, Multi-Curve Construction/Valuation, Collateral Valuation and XVA
 *  	Metric Generation, Calibration and Hedge Attributions, Statistical Curve Construction, Bond RV
 *  	Metrics, Stochastic Evolution and Option Pricing, Interest Rate Dynamics and Option Pricing, LMM
 *  	Extensions/Calibrations/Greeks, Algorithmic Differentiation, and Asset Backed Models and Analytics.
 * 
 *  - DRIP Asset Allocation: Library for model libraries for MPT framework, Black Litterman Strategy
 *  	Incorporator, Holdings Constraint, and Transaction Costs.
 * 
 *  - DRIP Numerical Optimizer: Library for Numerical Optimization and Spline Functionality.
 * 
 *  - DRIP Statistical Learning: Library for Statistical Evaluation and Machine Learning.
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
 * UST05Y demonstrates the Details behind the Implementation and the Pricing of the 5Y FV1 UST Futures
 *  Contract.
 *
 * @author Lakshmi Krishnamurthy
 */

public class UST05Y {

	private static final MergedDiscountForwardCurve FundingCurve (
		final JulianDate dtSpot,
		final String strCurrency)
		throws Exception
	{
		String[] astrDepositMaturityTenor = new String[] {
			"2D",
			"1W",
			"1M",
			"2M",
			"3M"
		};

		double[] adblDepositQuote = new double[] {
			0.00195, // 2D
			0.00176, // 1W
			0.00301, // 1M
			0.00401, // 2M
			0.00492  // 3M
		};

		double[] adblFuturesQuote = new double[] {
			0.00609,
			0.00687
		};

		String[] astrFixFloatMaturityTenor = new String[] {
			"01Y",
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
			0.00762, //  1Y
			0.01055, //  2Y
			0.01300, //  3Y
			0.01495, //  4Y
			0.01651, //  5Y
			0.01787, //  6Y
			0.01904, //  7Y
			0.02005, //  8Y
			0.02090, //  9Y
			0.02166, // 10Y
			0.02231, // 11Y
			0.02289, // 12Y
			0.02414, // 15Y
			0.02570, // 20Y
			0.02594, // 25Y
			0.02627, // 30Y
			0.02648, // 40Y
			0.02632  // 50Y
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

		System.out.println ("\n\n\t|------------------------------------||");

		System.out.println ("\t|       DEPOSIT INPUT vs. CALC       ||");

		System.out.println ("\t|------------------------------------||");

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

		System.out.println ("\t|------------------------------------||");

		System.out.println ("\n\t|------------------------------------||");

		System.out.println ("\t|       FUTURES INPUT vs. CALC       ||");

		System.out.println ("\t|------------------------------------||");

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

		System.out.println ("\t|------------------------------------||");

		System.out.println ("\n\t|-----------------------------------------------|| ");

		System.out.println ("\t|         FIX-FLOAT INPUTS vs CALIB             ||");

		System.out.println ("\t|-----------------------------------------------|| ");

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

		System.out.println ("\t|-----------------------------------------------|| \n");

		return dcFunding;
	}

	private static final void OnTheRunQuote (
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

	private static final void FuturesQuote (
		final CurveSurfaceQuoteContainer csqc,
		final TreasuryFutures bf,
		final double dblFuturesPrice)
		throws Exception
	{
		ProductMultiMeasure pmmq = new ProductMultiMeasure();

		pmmq.addQuote (
			"Price",
			new MultiSided (
				"mid",
				dblFuturesPrice
			),
			true
		);

		csqc.setProductQuote (
			bf.name(),
			pmmq
		);
	}

	private static final void RepoCurves (
		final JulianDate dtSpot,
		final CurveSurfaceQuoteContainer csqc,
		final Bond[] aBond,
		final double[] adblRepoRate)
		throws Exception
	{
		for (int i = 0; i < aBond.length; ++i)
			csqc.setRepoState (
				ScenarioRepoCurveBuilder.FlatRateRepoCurve (
					dtSpot,
					aBond[i],
					adblRepoRate[i]
				)
			);
	}

	private static final GovvieCurve TreasuryCurve (
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
			dtSpot
		};

		JulianDate[] adtMaturity = new JulianDate[] {
			dtSpot.addTenor ("1Y"),
			dtSpot.addTenor ("2Y"),
			dtSpot.addTenor ("3Y"),
			dtSpot.addTenor ("5Y"),
			dtSpot.addTenor ("7Y"),
			dtSpot.addTenor ("10Y"),
			dtSpot.addTenor ("30Y")
		};

		GovvieCurve gc = LatentMarketStateBuilder.ShapePreservingGovvieCurve (
			strCode,
			dtSpot,
			adtEffective,
			adtMaturity,
			adblCoupon,
			adblYield,
			"Yield"
		);

		BondComponent[] aComp = TreasuryBuilder.FromCode (
			strCode,
			adtEffective,
			adtMaturity,
			adblCoupon
		);

		ValuationParams valParams = new ValuationParams (
			dtSpot,
			dtSpot,
			"USD"
		);

		CurveSurfaceQuoteContainer csqc = new CurveSurfaceQuoteContainer();

		csqc.setGovvieState (gc);

		System.out.println ("\n\t|--------------------------------------------||");

		System.out.println ("\t|       TREASURY INPUT vs CALIB YIELD        ||");

		System.out.println ("\t|--------------------------------------------||");

		for (int i = 0; i < aComp.length; ++i)
			System.out.println ("\t| " + aComp[i].name() + " | " +
				FormatUtil.FormatDouble (adblYield[i], 2, 2, 100.) + "% | " +
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
						gc.yield (aComp[i].maturityDate().julian())
					)
				), 2, 2, 100.) + "% ||"
			);

		System.out.println ("\t|--------------------------------------------||");

		return gc;
	}

	private static final void AccumulateBondMarketQuote (
		final CurveSurfaceQuoteContainer csqc,
		final Bond[] aBond,
		final double[] adblCleanPrice)
		throws Exception
	{
		for (int i = 0; i < aBond.length; ++i) {
			ProductMultiMeasure pmmq = new ProductMultiMeasure();

			pmmq.addQuote (
				"Price",
				new MultiSided (
					"mid",
					adblCleanPrice[i]
				),
				true
			);

			csqc.setProductQuote (
				aBond[i].name(),
				pmmq
			);
		}
	}

	/*
	 * Print the Bond RV Measures
	 * 
	 *  	USE WITH CARE: This sample ignores errors and does not handle exceptions.
	 */

	private static final boolean PrintRVMeasures (
		final String strPrefix,
		final BondRVMeasures rv)
		throws Exception
	{
		if (null == rv) return false;

		System.out.println (strPrefix + "ASW               : " + FormatUtil.FormatDouble (rv.asw(), 2, 0, 10000.));

		System.out.println (strPrefix + "Bond Basis        : " + FormatUtil.FormatDouble (rv.bondBasis(), 2, 0, 10000.));

		System.out.println (strPrefix + "Convexity         : " + FormatUtil.FormatDouble (rv.convexity(), 1, 4, 1000000.));

		System.out.println (strPrefix + "Discount Margin   : " + FormatUtil.FormatDouble (rv.discountMargin(), 2, 0, 10000.));

		System.out.println (strPrefix + "G Spread          : " + FormatUtil.FormatDouble (rv.gSpread(), 2, 0, 10000.));

		System.out.println (strPrefix + "I Spread          : " + FormatUtil.FormatDouble (rv.iSpread(), 2, 0, 10000.));

		System.out.println (strPrefix + "Macaulay Duration : " + FormatUtil.FormatDouble (rv.macaulayDuration(), 1, 4, 1.));

		System.out.println (strPrefix + "Modified Duration : " + FormatUtil.FormatDouble (rv.modifiedDuration(), 1, 4, 10000.));

		System.out.println (strPrefix + "Price             : " + FormatUtil.FormatDouble (rv.price(), 2, 4, 100.));

		System.out.println (strPrefix + "Workout Date      : " + new JulianDate (rv.wi().date()));

		System.out.println (strPrefix + "Workout Factor    : " + FormatUtil.FormatDouble (rv.wi().factor(), 2, 4, 1.));

		System.out.println (strPrefix + "Workout Type      : " + rv.wi().type());

		System.out.println (strPrefix + "Workout Yield     : " + FormatUtil.FormatDouble (rv.wi().yield(), 1, 4, 100.) + "%");

		System.out.println (strPrefix + "Yield01           : " + FormatUtil.FormatDouble (rv.yield01(), 1, 4, 10000.));

		System.out.println (strPrefix + "Yield Basis       : " + FormatUtil.FormatDouble (rv.bondBasis(), 2, 0, 10000.));

		System.out.println (strPrefix + "Yield Spread      : " + FormatUtil.FormatDouble (rv.bondBasis(), 2, 0, 10000.));

		System.out.println (strPrefix + "Z Spread          : " + FormatUtil.FormatDouble (rv.zSpread(), 2, 0, 10000.));

		return true;
	}

	private static final void BondRVMeasuresSample (
		final BondComponent bond,
		final JulianDate dtSpot,
		final CurveSurfaceQuoteContainer csqc,
		final double dblPrice)
		throws Exception
	{

		ValuationParams valParams = ValuationParams.Spot (
			dtSpot,
			0,
			"",
			Convention.DATE_ROLL_ACTUAL
		);

		/*
		 * Compute the work-out date given the price.
		 */

		WorkoutInfo wi = bond.exerciseYieldFromPrice (
			valParams,
			csqc,
			null,
			dblPrice
		);

		/*
		 * Compute the base RV measures to the work-out date.
		 */

		org.drip.analytics.output.BondRVMeasures rvm = bond.standardMeasures (
			valParams,
			null,
			csqc,
			null,
			wi,
			dblPrice
		);

		System.out.println ("\t|---------------------------------------------||\n");

		PrintRVMeasures ("\t|\t", rvm);

		Map<String, Double> mapOutput = bond.value (
			valParams,
			null,
			csqc,
			null
		);

		System.out.println ("\t|---------------------------------------------||\n");

		System.out.println ("\n\t|--------------------------------------------------------------------------||");

		System.out.println ("\t|                  CTD Full Bond Measures                                  ||");

		System.out.println ("\t|--------------------------------------------------------------------------||");

		for (Map.Entry<String, Double> me : mapOutput.entrySet())
			System.out.println ("\t|\t" + me.getKey() + " => " + me.getValue());

		System.out.println ("\t|--------------------------------------------------------------------------||");
	}

	private static final void ComputeFuturesMeasures (
		final TreasuryFutures bf,
		final JulianDate dtSpot,
		final CurveSurfaceQuoteContainer csqc,
		final double[] adblCleanPrice)
		throws Exception
	{
		ValuationParams valParams = ValuationParams.Spot (
			dtSpot,
			0,
			"",
			Convention.DATE_ROLL_ACTUAL
		);

		AccumulateBondMarketQuote (
			csqc,
			bf.basket(),
			adblCleanPrice
		);

		Map<String, Double> mapOutput = bf.value (
			valParams,
			null,
			csqc,
			null
		);

		System.out.println ("\n\t|--------------------------------------------------------------------------||");

		System.out.println ("\t|                  Bond Futures Measures                                   ||");

		System.out.println ("\t|--------------------------------------------------------------------------||");

		for (Map.Entry<String, Double> me : mapOutput.entrySet())
			System.out.println ("\t|\t" + me.getKey() + " => " + me.getValue());

		System.out.println ("\t|--------------------------------------------------------------------------||");
	}

	public static final void main (
		final String[] astrArgs)
		throws Exception
	{
		EnvManager.InitEnv ("");

		JulianDate dtSpot = DateUtil.CreateFromYMD (
			2015,
			DateUtil.NOVEMBER,
			18
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
			0.0300
		};

		double[] adblTreasuryYield = new double[] {
			0.00692,
			0.00945,
			0.01257,
			0.01678,
			0.02025,
			0.02235,
			0.02972
		};

		GovvieCurve gc = TreasuryCurve (
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

		csqc.setGovvieState (gc);

		OnTheRunQuote (
			csqc,
			new String[] {
				"01YON",
				"02YON",
				"03YON",
				"05YON",
				"07YON",
				"10YON",
				"30YON"
			},
			adblTreasuryYield
		);

		Bond ust912828J5 = TreasuryBuilder.UST (
			DateUtil.CreateFromYMD (
				2014,
				DateUtil.FEBRUARY,
				28
			),
			DateUtil.CreateFromYMD (
				2020,
				DateUtil.FEBRUARY,
				29
			),
			0.01375
		);

		Bond ust912828J8 = TreasuryBuilder.UST (
			DateUtil.CreateFromYMD (
				2014,
				DateUtil.MARCH,
				31
			),
			DateUtil.CreateFromYMD (
				2020,
				DateUtil.MARCH,
				31
			),
			0.01375
		);

		Bond ust912828K5 = TreasuryBuilder.UST (
			DateUtil.CreateFromYMD (
				2014,
				DateUtil.APRIL,
				30
			),
			DateUtil.CreateFromYMD (
				2020,
				DateUtil.APRIL,
				30
			),
			0.01375
		);

		Bond ust912828XE = TreasuryBuilder.UST (
			DateUtil.CreateFromYMD (
				2014,
				DateUtil.MAY,
				31
			),
			DateUtil.CreateFromYMD (
				2020,
				DateUtil.MAY,
				31
			),
			0.01500
		);

		Bond ust912828XH = TreasuryBuilder.UST (
			DateUtil.CreateFromYMD (
				2014,
				DateUtil.JUNE,
				30
			),
			DateUtil.CreateFromYMD (
				2020,
				DateUtil.JUNE,
				30
			),
			0.01625
		);

		Bond ust912828XM = TreasuryBuilder.UST (
			DateUtil.CreateFromYMD (
				2014,
				DateUtil.JULY,
				31
			),
			DateUtil.CreateFromYMD (
				2020,
				DateUtil.JULY,
				31
			),
			0.01625
		);

		Bond ust912828L3 = TreasuryBuilder.UST (
			DateUtil.CreateFromYMD (
				2014,
				DateUtil.AUGUST,
				31
			),
			DateUtil.CreateFromYMD (
				2020,
				DateUtil.AUGUST,
				31
			),
			0.01375
		);

		Bond ust912828L6 = TreasuryBuilder.UST (
			DateUtil.CreateFromYMD (
				2014,
				DateUtil.SEPTEMBER,
				30
			),
			DateUtil.CreateFromYMD (
				2020,
				DateUtil.SEPTEMBER,
				30
			),
			0.01375
		);

		Bond ust912828L9 = TreasuryBuilder.UST (
			DateUtil.CreateFromYMD (
				2014,
				DateUtil.OCTOBER,
				31
			),
			DateUtil.CreateFromYMD (
				2020,
				DateUtil.OCTOBER,
				31
			),
			0.01375
		);

		double dblContractSize = 100000.;
		double dblFuturesPrice = 1.1900000;

		Bond[] aBond = new Bond[] {
			ust912828J5,
			ust912828J8,
			ust912828K5,
			ust912828XE,
			ust912828XH,
			ust912828XM,
			ust912828L3,
			ust912828L6,
			ust912828L9
		};

		TreasuryFutures ust5YFutures = new TreasuryFutures (
			aBond,
			new double[] {
				0.8317, // 912828J5
				0.8287, // 912828J8
				0.8258, // 912828K5
				0.8276, // 912828XE
				0.8297, // 912828XH
				0.8269, // 912828XM
				0.8141, // 912828L3
				0.8113, // 912828L6
				0.8084	// 912828L9
			},
			null
		);

		double[] adblRepoRate = new double[] {
			 0.00800,
			 0.00825,
			 0.00850,
			 0.00875,
			 0.00900,
			 0.00925,
			 0.00950,
			 0.00975,
			 0.01000
		};

		RepoCurves (
			dtSpot,
			csqc,
			aBond,
			adblRepoRate
		);

		ust5YFutures.setExpiry (
			DateUtil.CreateFromYMD (
				2016,
				DateUtil.FEBRUARY,
				15
			)
		);

		double[] adblCleanPrice = new double[] {
			 0.9909375,
			 0.9900000,
			 0.9890625,
			 0.9943750,
			 0.9984375,
			 0.9978125,
			 0.9862500,
			 0.9850000,
			 0.9853125
		};

		FuturesQuote (
			csqc,
			ust5YFutures,
			dblFuturesPrice
		);

		Bond bondCTD = ust5YFutures.cheapestToDeliverYield (
			dtSpot.julian(),
			adblCleanPrice
		).bond();

		System.out.println ("\n\t|---------------------------------------------||");

		System.out.println ("\t|                                             ||");

		System.out.println ("\t|       Bond #1: " + ust912828J5.name() + "       ||");

		System.out.println ("\t|       Bond #2: " + ust912828J8.name() + "       ||");

		System.out.println ("\t|       Bond #3: " + ust912828K5.name() + "       ||");

		System.out.println ("\t|       Bond #4: " + ust912828XE.name() + "       ||");

		System.out.println ("\t|       Bond #5: " + ust912828XH.name() + "       ||");

		System.out.println ("\t|       Bond #6: " + ust912828XM.name() + "       ||");

		System.out.println ("\t|       Bond #7: " + ust912828L3.name() + "       ||");

		System.out.println ("\t|       Bond #8: " + ust912828L6.name() + "       ||");

		System.out.println ("\t|       Bond #9: " + ust912828L9.name() + "       ||");

		System.out.println ("\t|                                             ||");

		System.out.println ("\t|---------------------------------------------||");

		System.out.println ("\t| Cheapest to Deliver: " + bondCTD.name() + " ||");

		BondRVMeasuresSample (
			(BondComponent) bondCTD,
			dtSpot,
			csqc,
			0.9909375
		);

		ComputeFuturesMeasures (
			ust5YFutures,
			dtSpot,
			csqc,
			adblCleanPrice
		);

		System.out.println ("\n\t|---------------------------------------------||");

		System.out.println ("\t|      Futures Price  : " + FormatUtil.FormatDouble (dblFuturesPrice, 2, 5, 100.) + "            ||");

		System.out.println ("\t|      Contract Size  : " + FormatUtil.FormatDouble (dblContractSize, 1, 2, 1.) + "            ||");

		System.out.println ("\t|      Contract Value : " + FormatUtil.FormatDouble (dblContractSize * dblFuturesPrice, 1, 2, 1.) + "            ||");

		System.out.println ("\t|---------------------------------------------||\n");
	}
}
