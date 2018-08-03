
package org.drip.sample.simm20estimates;

import java.util.HashMap;
import java.util.Map;

import org.drip.quant.common.FormatUtil;
import org.drip.service.env.EnvManager;
import org.drip.simm20.product.CurveTenorSensitivitySettings;
import org.drip.simm20.product.IRNetMarginCovariance;
import org.drip.simm20.product.IRNetSensitivity;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2018 Lakshmi Krishnamurthy
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
 * IRNetSensitivityGroup computes the Weighted Net Sensitivities across each of the IR Risk Factors as a Full
 *  Group - OIS, LIBOR 1M, LIBOR 3M, LIBOR 6M LIBOR 12M, PRIME, and MUNICIPAL. The References are:
 *  
 *  - Andersen, L. B. G., M. Pykhtin, and A. Sokol (2017): Credit Exposure in the Presence of Initial Margin,
 *  	https://papers.ssrn.com/sol3/papers.cfm?abstract_id=2806156, eSSRN.
 *  
 *  - Albanese, C., S. Caenazzo, and O. Frankel (2017): Regression Sensitivities for Initial Margin
 *  	Calculations, https://papers.ssrn.com/sol3/papers.cfm?abstract_id=2763488, eSSRN.
 *  
 *  - Anfuso, F., D. Aziz, P. Giltinan, and K. Loukopoulus (2017): A Sound Modeling and Back-testing
 *  	Framework for Forecasting Initial Margin Requirements,
 *  	https://papers.ssrn.com/sol3/papers.cfm?abstract_id=2716279, eSSRN.
 *  
 *  - Caspers, P., P. Giltinan, R. Lichters, and N. Nowaczyk (2017): Forecasting Initial Margin Requirements
 *  	- A Model Evaluation https://papers.ssrn.com/sol3/papers.cfm?abstract_id=2911167, eSSRN.
 *  
 *  - International Swaps and Derivatives Association (2017): SIMM v2.0 Methodology,
 *  	https://www.isda.org/a/oFiDE/isda-simm-v2.pdf.
 * 
 * @author Lakshmi Krishnamurthy
 */

public class IRNetSensitivityGroup
{

	private static final Map<String, Double> TenorSensitivityMap (
		final double[] tenorSensitivities)
		throws Exception
	{
		Map<String, Double> oisTenorSensitivities = new HashMap<String, Double>();

		oisTenorSensitivities.put (
			"02W",
			tenorSensitivities[0]
		);

		oisTenorSensitivities.put (
			"01M",
			tenorSensitivities[1]
		);

		oisTenorSensitivities.put (
			"03M",
			tenorSensitivities[2]
		);

		oisTenorSensitivities.put (
			"06M",
			tenorSensitivities[3]
		);

		oisTenorSensitivities.put (
			"01Y",
			tenorSensitivities[4]
		);

		oisTenorSensitivities.put (
			"02Y",
			tenorSensitivities[5]
		);

		oisTenorSensitivities.put (
			"03Y",
			tenorSensitivities[6]
		);

		oisTenorSensitivities.put (
			"05Y",
			tenorSensitivities[7]
		);

		oisTenorSensitivities.put (
			"10Y",
			tenorSensitivities[8]
		);

		oisTenorSensitivities.put (
			"15Y",
			tenorSensitivities[9]
		);

		oisTenorSensitivities.put (
			"20Y",
			tenorSensitivities[10]
		);

		oisTenorSensitivities.put (
			"30Y",
			tenorSensitivities[11]
		);

		return oisTenorSensitivities;
	}

	public static final void main (
		final String[] inputs)
		throws Exception
	{
		EnvManager.InitEnv ("");

		String currency = "USD";

		CurveTenorSensitivitySettings curveTenorSensitivitySettings = CurveTenorSensitivitySettings.ISDA
			(currency);

		double[] oisTenorSensitivities = new double[]
		{
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
		};
		double[] libor1MTenorSensitivities = new double[]
		{
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
		};
		double[] libor3MTenorSensitivities = new double[] 
		{
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
		};
		double[] libor6MTenorSensitivities = new double[]
		{
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
		};
		double[] libor12MTenorSensitivities = new double[]
		{
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
		};
		double[] primeTenorSensitivities = new double[]
		{
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
		};
		double[] municipalTenorSensitivities = new double[]
		{
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
			10. * (Math.random() - 0.5),
		};

		IRNetSensitivity irNetSensitivity = new IRNetSensitivity (
			TenorSensitivityMap (oisTenorSensitivities),
			TenorSensitivityMap (libor1MTenorSensitivities),
			TenorSensitivityMap (libor3MTenorSensitivities),
			TenorSensitivityMap (libor6MTenorSensitivities),
			TenorSensitivityMap (libor12MTenorSensitivities),
			TenorSensitivityMap (primeTenorSensitivities),
			TenorSensitivityMap (municipalTenorSensitivities)
		);

		IRNetMarginCovariance irNetMarginCovariance = irNetSensitivity.marginCovariance
			(curveTenorSensitivitySettings);

		System.out.println (
			"\t| IM Covariance[     OIS   -   OIS     ] => " +
			FormatUtil.FormatDouble (
				irNetMarginCovariance.ois_ois(), 3, 2, 1.
			) + " ||"
		);

		System.out.println (
			"\t| IM Covariance[ LIBOR  1M - LIBOR  1M ] => " +
			FormatUtil.FormatDouble (
				irNetMarginCovariance.libor1M_libor1M(), 3, 2, 1.
			) + " ||"
		);

		System.out.println (
			"\t| IM Covariance[ LIBOR  3M - LIBOR  3M ] => " +
			FormatUtil.FormatDouble (
				irNetMarginCovariance.libor3M_libor3M(), 3, 2, 1.
			) + " ||"
		);

		System.out.println (
			"\t| IM Covariance[ LIBOR  6M - LIBOR  6M ] => " +
			FormatUtil.FormatDouble (
				irNetMarginCovariance.libor6M_libor6M(), 3, 2, 1.
			) + " ||"
		);

		System.out.println (
			"\t| IM Covariance[ LIBOR 12M - LIBOR 12M ] => " +
			FormatUtil.FormatDouble (
				irNetMarginCovariance.libor12M_libor12M(), 3, 2, 1.
			) + " ||"
		);

		System.out.println (
			"\t| IM Covariance[   PRIME   -   PRIME   ] => " +
			FormatUtil.FormatDouble (
				irNetMarginCovariance.prime_prime(), 3, 2, 1.
			) + " ||"
		);

		System.out.println (
			"\t| IM Covariance[ MUNICIPAL - MUNICIPAL ] => " +
			FormatUtil.FormatDouble (
				irNetMarginCovariance.municipal_municipal(), 3, 2, 1.
			) + " ||"
		);

		System.out.println (
			"\t| IM Covariance[     OIS   - LIBOR  1M ] => " +
			FormatUtil.FormatDouble (
				irNetMarginCovariance.ois_libor1M(), 3, 2, 1.
			) + " ||"
		);

		System.out.println (
			"\t| IM Covariance[     OIS   - LIBOR  3M ] => " +
			FormatUtil.FormatDouble (
				irNetMarginCovariance.ois_libor3M(), 3, 2, 1.
			) + " ||"
		);

		System.out.println (
			"\t| IM Covariance[     OIS   - LIBOR  6M ] => " +
			FormatUtil.FormatDouble (
				irNetMarginCovariance.ois_libor6M(), 3, 2, 1.
			) + " ||"
		);

		System.out.println (
			"\t| IM Covariance[     OIS   - LIBOR 12M ] => " +
			FormatUtil.FormatDouble (
				irNetMarginCovariance.ois_libor12M(), 3, 2, 1.
			) + " ||"
		);

		System.out.println (
			"\t| IM Covariance[     OIS   -   PRIME   ] => " +
			FormatUtil.FormatDouble (
				irNetMarginCovariance.ois_prime(), 3, 2, 1.
			) + " ||"
		);

		System.out.println (
			"\t| IM Covariance[     OIS   - MUNICIPAL ] => " +
			FormatUtil.FormatDouble (
				irNetMarginCovariance.ois_municipal(), 3, 2, 1.
			) + " ||"
		);

		System.out.println (
			"\t| IM Covariance[ LIBOR  1M - LIBOR  3M ] => " +
			FormatUtil.FormatDouble (
				irNetMarginCovariance.libor1M_libor3M(), 3, 2, 1.
			) + " ||"
		);

		System.out.println (
			"\t| IM Covariance[ LIBOR  1M - LIBOR  6M ] => " +
			FormatUtil.FormatDouble (
				irNetMarginCovariance.libor1M_libor6M(), 3, 2, 1.
			) + " ||"
		);

		System.out.println (
			"\t| IM Covariance[ LIBOR  1M - LIBOR 12M ] => " +
			FormatUtil.FormatDouble (
				irNetMarginCovariance.libor1M_libor12M(), 3, 2, 1.
			) + " ||"
		);

		System.out.println (
			"\t| IM Covariance[ LIBOR  1M -   PRIME   ] => " +
			FormatUtil.FormatDouble (
				irNetMarginCovariance.libor1M_prime(), 3, 2, 1.
			) + " ||"
		);

		System.out.println (
			"\t| IM Covariance[ LIBOR  1M - MUNICIPAL ] => " +
			FormatUtil.FormatDouble (
				irNetMarginCovariance.libor1M_municipal(), 3, 2, 1.
			) + " ||"
		);

		System.out.println (
			"\t| IM Covariance[ LIBOR  3M - LIBOR  6M ] => " +
			FormatUtil.FormatDouble (
				irNetMarginCovariance.libor3M_libor6M(), 3, 2, 1.
			) + " ||"
		);

		System.out.println (
			"\t| IM Covariance[ LIBOR  3M - LIBOR 12M ] => " +
			FormatUtil.FormatDouble (
				irNetMarginCovariance.libor3M_libor12M(), 3, 2, 1.
			) + " ||"
		);

		System.out.println (
			"\t| IM Covariance[ LIBOR  3M -   PRIME   ] => " +
			FormatUtil.FormatDouble (
				irNetMarginCovariance.libor3M_prime(), 3, 2, 1.
			) + " ||"
		);

		System.out.println (
			"\t| IM Covariance[ LIBOR  3M - MUNICIPAL ] => " +
			FormatUtil.FormatDouble (
				irNetMarginCovariance.libor3M_municipal(), 3, 2, 1.
			) + " ||"
		);

		EnvManager.TerminateEnv();
	}
}
