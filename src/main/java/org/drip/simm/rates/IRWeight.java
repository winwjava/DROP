
package org.drip.simm.rates;

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
 * IRWeight holds the ISDA SIMM Tenor Interest Rate Vertex Risk Weights for Currencies across all Volatility
 *  Types. The References are:
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

public class IRWeight
{
	private java.lang.String _volatilityType = "";

	private java.util.Map<java.lang.String, java.lang.Double> _tenorVega = new
		java.util.HashMap<java.lang.String, java.lang.Double>();

	private java.util.Map<java.lang.String, java.lang.Double> _tenorDelta = new
		java.util.HashMap<java.lang.String, java.lang.Double>();

	/**
	 * IRWeight Constructor
	 * 
	 * @param volatilityType The Volatility Type
	 * @param tenorDelta The Map of Tenor Delta Risk Weights
	 * @param tenorVega The Map of Tenor Vega Risk Weights
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public IRWeight (
		final java.lang.String volatilityType,
		final java.util.Map<java.lang.String, java.lang.Double> tenorDelta,
		final java.util.Map<java.lang.String, java.lang.Double> tenorVega)
		throws java.lang.Exception
	{
		if (null == (_volatilityType = volatilityType) || _volatilityType.isEmpty() ||
			null == (_tenorDelta = tenorDelta) ||
			null == (_tenorVega = tenorVega))
		{
			throw new java.lang.Exception ("IRWeight Constructor => Invalid Inputs");
		}

		int tenorCount = _tenorDelta.size();

		if (0 == tenorCount || tenorCount != _tenorVega.size())
		{
			throw new java.lang.Exception ("IRWeight Constructor => Invalid Inputs");
		}
	}

	/**
	 * Retrieve the Volatility Type
	 * 
	 * @return The Volatility Type
	 */

	public java.lang.String volatilityType()
	{
		return _volatilityType;
	}

	/**
	 * Retrieve the Tenor Delta Weight Map
	 * 
	 * @return The Tenor Delta Weight Map
	 */

	public java.util.Map<java.lang.String, java.lang.Double> tenorDelta()
	{
		return _tenorDelta;
	}

	/**
	 * Retrieve the Tenor Vega Weight Map
	 * 
	 * @return The Tenor Vega Weight Map
	 */

	public java.util.Map<java.lang.String, java.lang.Double> tenorVega()
	{
		return _tenorVega;
	}

	/**
	 * Retrieve the Tenors
	 * 
	 * @return The Tenors
	 */

	public java.util.Set<java.lang.String> tenors()
	{
		return _tenorDelta.keySet();
	}
}