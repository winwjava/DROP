
package org.drip.simm.commodity;

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
 * CTRiskThresholdContainer20 holds the ISDA SIMM 2.0 Commodity Risk Thresholds - the Commodity Buckets and
 *  the Delta/Vega Limits defined for the Concentration Thresholds. The References are:
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

public class CTRiskThresholdContainer20
{
	private static final java.util.Map<java.lang.Integer, org.drip.simm.common.DeltaVegaThreshold>
		s_DeltaVegaThresholdMap = new java.util.TreeMap<java.lang.Integer,
			org.drip.simm.common.DeltaVegaThreshold>();

	/**
	 * Initialize the Commodity Risk Threshold Container
	 * 
	 * @return TRUE - The Commodity Risk Threshold Container successfully initialized
	 */

	public static final boolean Init()
	{
		try
		{
			s_DeltaVegaThresholdMap.put (
				1,
				new org.drip.simm.common.DeltaVegaThreshold (
					1400.,
					250.
				)
			);

			s_DeltaVegaThresholdMap.put (
				2,
				new org.drip.simm.common.DeltaVegaThreshold (
					20000.,
					2000.
				)
			);

			s_DeltaVegaThresholdMap.put (
				3,
				new org.drip.simm.common.DeltaVegaThreshold (
					3500.,
					510.
				)
			);

			s_DeltaVegaThresholdMap.put (
				4,
				new org.drip.simm.common.DeltaVegaThreshold (
					3500.,
					510.
				)
			);

			s_DeltaVegaThresholdMap.put (
				5,
				new org.drip.simm.common.DeltaVegaThreshold (
					3500.,
					510.
				)
			);

			s_DeltaVegaThresholdMap.put (
				6,
				new org.drip.simm.common.DeltaVegaThreshold (
					6400.,
					1900.
				)
			);

			s_DeltaVegaThresholdMap.put (
				7,
				new org.drip.simm.common.DeltaVegaThreshold (
					6400.,
					1900.
				)
			);

			s_DeltaVegaThresholdMap.put (
				8,
				new org.drip.simm.common.DeltaVegaThreshold (
					2500.,
					870.
				)
			);

			s_DeltaVegaThresholdMap.put (
				9,
				new org.drip.simm.common.DeltaVegaThreshold (
					2500.,
					870.
				)
			);

			s_DeltaVegaThresholdMap.put (
				10,
				new org.drip.simm.common.DeltaVegaThreshold (
					300.,
					220.
				)
			);

			s_DeltaVegaThresholdMap.put (
				11,
				new org.drip.simm.common.DeltaVegaThreshold (
					2900.,
					450.
				)
			);

			s_DeltaVegaThresholdMap.put (
				12,
				new org.drip.simm.common.DeltaVegaThreshold (
					7600.,
					740.
				)
			);

			s_DeltaVegaThresholdMap.put (
				13,
				new org.drip.simm.common.DeltaVegaThreshold (
					3900.,
					370.
				)
			);

			s_DeltaVegaThresholdMap.put (
				14,
				new org.drip.simm.common.DeltaVegaThreshold (
					3900.,
					370.
				)
			);

			s_DeltaVegaThresholdMap.put (
				15,
				new org.drip.simm.common.DeltaVegaThreshold (
					3900.,
					370.
				)
			);

			s_DeltaVegaThresholdMap.put (
				16,
				new org.drip.simm.common.DeltaVegaThreshold (
					300.,
					220.
				)
			);

			s_DeltaVegaThresholdMap.put (
				17,
				new org.drip.simm.common.DeltaVegaThreshold (
					12000.,
					430.
				)
			);
		}
		catch (java.lang.Exception e)
		{
			e.printStackTrace();

			return false;
		}

		return true;
	}

	/**
	 * Retrieve the Commodity Risk Threshold Bucket Set
	 * 
	 * @return The Commodity Risk Threshold Bucket Set
	 */

	public static final java.util.Set<java.lang.Integer> BucketSet()
	{
		return s_DeltaVegaThresholdMap.keySet();
	}

	/**
	 * Indicate if the Bucket Number is available in the Commodity Risk Threshold Container
	 * 
	 * @param bucketNumber The Bucket Number
	 * 
	 * @return TRUE - The Bucket Number is available in the Commodity Risk Threshold Container
	 */

	public static final boolean ContainsBucket (
		final int bucketNumber)
	{
		return s_DeltaVegaThresholdMap.containsKey (bucketNumber);
	}

	/**
	 * Retrieve the Threshold indicated by the Bucket Number
	 * 
	 * @param bucketNumber The Bucket Number
	 * 
	 * @return The Threshold indicated by the Bucket Number
	 */

	public static final org.drip.simm.common.DeltaVegaThreshold Threshold (
		final int bucketNumber)
	{
		return ContainsBucket (bucketNumber) ? s_DeltaVegaThresholdMap.get (bucketNumber) : null;
	}

	/**
	 * Retrieve the Delta Vega Threshold Map
	 * 
	 * @return The Delta Vega Threshold Map
	 */

	public static final java.util.Map<java.lang.Integer, org.drip.simm.common.DeltaVegaThreshold>
		DeltaVegaThresholdMap()
	{
		return s_DeltaVegaThresholdMap;
	}
}