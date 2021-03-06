
package org.drip.sample.forward;

import org.drip.analytics.date.*;
import org.drip.function.r1tor1.QuadraticRationalShapeControl;
import org.drip.service.env.EnvManager;
import org.drip.spline.basis.PolynomialFunctionSetParams;
import org.drip.spline.params.*;
import org.drip.spline.stretch.MultiSegmentSequenceBuilder;
import org.drip.state.discount.*;
import org.drip.state.forward.ForwardCurve;
import org.drip.state.identifier.ForwardLabel;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2019 Lakshmi Krishnamurthy
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * Copyright (C) 2017 Lakshmi Krishnamurthy
 * Copyright (C) 2016 Lakshmi Krishnamurthy
 * Copyright (C) 2015 Lakshmi Krishnamurthy
 * Copyright (C) 2014 Lakshmi Krishnamurthy
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
 * <i>IBOR12MQuarticPolyVanilla</i> illustrates the Construction and Usage of the IBOR 12M Forward Curve
 * Using Vanilla Quartic Polynomial.
 *  
 * <br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/AnalyticsCore.md">Analytics Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/FixedIncomeAnalyticsLibrary.md">Fixed Income Analytics Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/sample/README.md">Sample</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/sample/forward/README.md">Forward Rate Curve Construction</a></li>
 *  </ul>
 * <br><br>
 * 
 * @author Lakshmi Krishnamurthy
 */

public class IBOR12MQuarticPolyVanilla {
	public static final void main (
		final String[] astrArgs)
		throws Exception
	{
		/*
		 * Initialize the Credit Analytics Library
		 */

		EnvManager.InitEnv ("");

		JulianDate dtValue = DateUtil.CreateFromYMD (
			2012,
			DateUtil.DECEMBER,
			11
		);

		String strTenor = "12M";
		String strCurrency = "USD";

		ForwardLabel fri = ForwardLabel.Create (
			strCurrency,
			strTenor
		);

		MergedDiscountForwardCurve dcEONIA = OvernightIndexCurve.MakeDC (
			dtValue,
			strCurrency
		);

		SegmentCustomBuilderControl scbcQuartic = new SegmentCustomBuilderControl (
			MultiSegmentSequenceBuilder.BASIS_SPLINE_POLYNOMIAL,
			new PolynomialFunctionSetParams (5),
			SegmentInelasticDesignControl.Create (
				2,
				2
			),
			new ResponseScalingShapeControl (
				true,
				new QuadraticRationalShapeControl (0.)
			),
			null
		);

		/*
		 * Construct the Array of Deposit Instruments and their Quotes from the given set of parameters
		 */

		double[] adblDepositQuote = new double[] {
			0.006537,
			0.006187,
			0.005772,
			0.005563,
			0.005400
		};

		String[] astrDepositTenor = new String[] {
			 "1M",
			 "3M",
			 "6M",
			 "9M",
			"12M"
		};

		/*
		 * Construct the Array of Fix-Float Component and their Quotes from the given set of parameters
		 */

		double[] adblFRAQuote = new double[] {
			0.004974,
			0.004783,
			0.004822,
			0.005070,
			0.005481,
			0.006025
		};

		String[] astrFRATenor = new String[] {
			 "3M",
			 "6M",
			 "9M",
			"12M",
			"15M",
			"18M",
		};

		/*
		 * Construct the Array of Float-Float Component and their Quotes from the given set of parameters
		 */

		double[] adblFloatFloatQuote = new double[] {
			-0.002070,	//  3Y
			-0.001640,	//  4Y
			-0.001510,	//  5Y
			-0.001390,	//  6Y
			-0.001300,	//  7Y
			-0.001230,	//  8Y
			-0.001180,	//  9Y
			-0.001130,	// 10Y
			-0.001090,	// 11Y
			-0.001060,	// 12Y
			-0.000930,	// 15Y
			-0.000800,	// 20Y
			-0.000720,	// 25Y
			-0.000660	// 30Y
		};

		String[] astrFloatFloatTenor = new String[] {
			  "3Y",
			  "4Y",
			  "5Y",
			  "6Y",
			  "7Y",
			  "8Y",
			  "9Y",
			 "10Y",
			 "11Y",
			 "12Y",
			 "15Y",
			 "20Y",
			 "25Y",
			 "30Y"
		};

		/*
		 * Construct the Array of Terminal Synthetic Float-Float Components and their Quotes from the given set of parameters
		 */

		String[] astrSyntheticFloatFloatTenor = new String[] {
			"35Y",
			"40Y",
			"50Y",
			"60Y"
		};

		double[] adblSyntheticFloatFloatQuote = new double[] {
			-0.000660,
			-0.000660,
			-0.000660,
			-0.000660
		};

		ForwardCurve fc6M = IBOR6MCubicPolyVanilla.Make6MForward (
			dtValue,
			strCurrency,
			"6M",
			true
		);

		ForwardCurve fc12M = IBORCurve.CustomIBORBuilderSample (
			dcEONIA,
			fc6M,
			fri,
			scbcQuartic,
			astrDepositTenor,
			adblDepositQuote,
			"ForwardRate",
			astrFRATenor,
			adblFRAQuote,
			"ParForwardRate",
			null,
			null,
			"SwapRate",
			astrFloatFloatTenor,
			adblFloatFloatQuote,
			"ReferenceParBasisSpread",
			astrSyntheticFloatFloatTenor,
			adblSyntheticFloatFloatQuote,
			"ReferenceParBasisSpread",
			"---- VANILLA QUARTIC POLYNOMIAL FORWARD CURVE ---",
			true
		);

		IBORCurve.ForwardJack (
			dtValue,
			"---- VANILLA QUARTIC POLYNOMIAL FORWARD CURVE SENSITIVITY ---",
			fc12M,
			"DerivedParBasisSpread"
		);

		EnvManager.TerminateEnv();
	}
}
