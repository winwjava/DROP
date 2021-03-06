
package org.drip.historical.state;

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
 * <i>CreditCurveMetrics</i> holds the computed Metrics associated the Credit Curve State.
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/NumericalCore.md">Numerical Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/AlgorithmSupportLibrary.md">Algorithm Support</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/historical/README.md">Historical</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/historical/state/README.md">State</a></li>
 *  </ul>
 *
 * @author Lakshmi Krishnamurthy
 */

public class CreditCurveMetrics {
	private org.drip.analytics.date.JulianDate _dtClose = null;

	private java.util.TreeMap<org.drip.analytics.date.JulianDate, java.lang.Double> _mapSurvivalProbability =
		new java.util.TreeMap<org.drip.analytics.date.JulianDate, java.lang.Double>();

	private java.util.TreeMap<org.drip.analytics.date.JulianDate, java.lang.Double> _mapRecoveryRate = new
		java.util.TreeMap<org.drip.analytics.date.JulianDate, java.lang.Double>();

	/**
	 * CreditCurveMetrics Constructor
	 * 
	 * @param dtClose The Closing Date
	 * 
	 * @throws java.lang.Exception Thrown if Inputs are Invalid
	 */

	public CreditCurveMetrics (
		final org.drip.analytics.date.JulianDate dtClose)
		throws java.lang.Exception
	{
		if (null == (_dtClose = dtClose))
			throw new java.lang.Exception ("CreditCurveMetrics Constructor => Invalid Inputs");
	}

	/**
	 * Retrieve the Closing Date
	 * 
	 * @return The Closing Date
	 */

	public org.drip.analytics.date.JulianDate close()
	{
		return _dtClose;
	}

	/**
	 * Add the Survival Probability corresponding to the specified Date
	 * 
	 * @param dt The Date
	 * @param dblSurvivalProbability The Survival Probability
	 * 
	 * @return TRUE - The Dated Survival Probability successfully added
	 */

	public boolean addSurvivalProbability (
		final org.drip.analytics.date.JulianDate dt,
		final double dblSurvivalProbability)
	{
		if (null == dt || !org.drip.numerical.common.NumberUtil.IsValid (dblSurvivalProbability)) return false;

		_mapSurvivalProbability.put (dt, dblSurvivalProbability);

		return true;
	}

	/**
	 * Add the Recovery Rate corresponding to the specified Date
	 * 
	 * @param dt The Date
	 * @param dblRecoveryRate The Recovery Rate
	 * 
	 * @return TRUE - The Dated Recovery Rate successfully added
	 */

	public boolean addRecoveryRate (
		final org.drip.analytics.date.JulianDate dt,
		final double dblRecoveryRate)
	{
		if (null == dt || !org.drip.numerical.common.NumberUtil.IsValid (dblRecoveryRate)) return false;

		_mapRecoveryRate.put (dt, dblRecoveryRate);

		return true;
	}

	/**
	 * Retrieve the Survival Probability corresponding to the specified Date
	 * 
	 * @param dt The Specified Date
	 * 
	 * @return The corresponding Survival Probability
	 * 
	 * @throws java.lang.Exception Thrown if the Survival Probability cannot be retrieved
	 */

	public double survivalProbability (
		final org.drip.analytics.date.JulianDate dt)
		throws java.lang.Exception
	{
		if (null == dt || !_mapSurvivalProbability.containsKey (dt))
			throw new java.lang.Exception ("CreditCurveMetrics::survivalProbability => Invalid Inputs");

		return _mapSurvivalProbability.get (dt);
	}

	/**
	 * Retrieve the Recovery Rate corresponding to the specified Date
	 * 
	 * @param dt The Specified Date
	 * 
	 * @return The corresponding Recovery Rate
	 * 
	 * @throws java.lang.Exception Thrown if the Recovery Rate cannot be retrieved
	 */

	public double recoveryRate (
		final org.drip.analytics.date.JulianDate dt)
		throws java.lang.Exception
	{
		if (null == dt || !_mapRecoveryRate.containsKey (dt))
			throw new java.lang.Exception ("CreditCurveMetrics::recoveryRate => Invalid Inputs");

		return _mapRecoveryRate.get (dt);
	}
}
