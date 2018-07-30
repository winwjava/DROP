
package org.drip.simm20.equity;

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
 * EQBucket holds the ISDA SIMM 2.0 Region, Sector, Member Correlation, and Risk Weights for a given Equity
 *  Issuer Exposure Bucket. The References are:
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

public class EQBucket
{
	private int _number = -1;
	private java.lang.String _size = "";
	private java.lang.String _region = "";
	private java.lang.String[] _sectorArray = null;
	private double _riskWeight = java.lang.Double.NaN;
	private double _vegaRiskWeight = java.lang.Double.NaN;
	private double _memberCorrelation = java.lang.Double.NaN;

	/**
	 * EQBucket Constructor
	 * 
	 * @param number Bucket Number
	 * @param size Bucket Equity Market Capitalization Size
	 * @param region Buket Region
	 * @param sectorArray Bucket Sector Array
	 * @param riskWeight Bucket Risk Weight
	 * @param memberCorrelation Bucket Member Correlation
	 * @param vegaRiskWeight The Vega Risk Weight
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public EQBucket (
		final int number,
		final java.lang.String size,
		final java.lang.String region,
		final java.lang.String[] sectorArray,
		final double riskWeight,
		final double memberCorrelation,
		final double vegaRiskWeight)
		throws java.lang.Exception
	{
		if (null == (_size = size) || _size.isEmpty() ||
			null == (_region = region) || _region.isEmpty() ||
			null == (_sectorArray = sectorArray) || 0 == _sectorArray.length ||
			!org.drip.quant.common.NumberUtil.IsValid (_riskWeight = riskWeight) ||
			!org.drip.quant.common.NumberUtil.IsValid (_memberCorrelation = memberCorrelation) ||
			!org.drip.quant.common.NumberUtil.IsValid (_vegaRiskWeight = vegaRiskWeight))
		{
			throw new java.lang.Exception ("EQBucket Constructor => Invalid Inputs");
		}

		_number = number;
	}

	/**
	 * Retrieve the Bucket Number
	 * 
	 * @return The Bucket Number
	 */

	public int number()
	{
		return _number;
	}

	/**
	 * Retrieve the Bucket Size
	 * 
	 * @return The Bucket Size
	 */

	public java.lang.String size()
	{
		return _size;
	}

	/**
	 * Retrieve the Bucket Region
	 * 
	 * @return The Bucket Region
	 */

	public java.lang.String region()
	{
		return _region;
	}

	/**
	 * Retrieve the Bucket Sector Array
	 * 
	 * @return The Bucket Sector Array
	 */

	public java.lang.String[] sectorArray()
	{
		return _sectorArray;
	}

	/**
	 * Retrieve the Bucket Risk Weight
	 * 
	 * @return The Bucket Risk Weight
	 */

	public double riskWeight()
	{
		return _riskWeight;
	}

	/**
	 * Retrieve the Correlation between the Bucket Members
	 * 
	 * @return Correlation between the Bucket Members
	 */

	public double memberCorrelation()
	{
		return _memberCorrelation;
	}

	/**
	 * Retrieve the Bucket Vega Risk Weight
	 * 
	 * @return The Bucket Vega Risk Weight
	 */

	public double vegaRiskWeight()
	{
		return _vegaRiskWeight;
	}
}