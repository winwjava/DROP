
package org.drip.dynamics.hullwhite;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2019 Lakshmi Krishnamurthy
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * Copyright (C) 2017 Lakshmi Krishnamurthy
 * Copyright (C) 2016 Lakshmi Krishnamurthy
 * Copyright (C) 2015 Lakshmi Krishnamurthy
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
 * <i>TrinomialTreeNodeMetrics</i> records the Metrics associated with each Node in the Trinomial Tree
 * Evolution of the Instantaneous Short Rate using the Hull-White Model.
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/AnalyticsCore.md">Analytics Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/FixedIncomeAnalyticsLibrary.md">Fixed Income Analytics</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/dynamics/README.md">Dynamics</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/dynamics/hullwhite/README.md">Hull White</a></li>
 *  </ul>
 *
 * @author Lakshmi Krishnamurthy
 */

public class TrinomialTreeNodeMetrics {
	private long _lTimeIndex = -1L;
	private long _lXStochasticIndex = 0L;
	private double _dblX = java.lang.Double.NaN;
	private double _dblAlpha = java.lang.Double.NaN;

	/**
	 * TrinomialTreeNodeMetrics Constructor
	 * 
	 * @param lTimeIndex The Tree Node's Time Index
	 * @param lXStochasticIndex The Tree Node's Stochastic Index
	 * @param dblX X
	 * @param dblAlpha Alpha
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public TrinomialTreeNodeMetrics (
		final long lTimeIndex,
		final long lXStochasticIndex,
		final double dblX,
		final double dblAlpha)
		throws java.lang.Exception
	{
		if (0 > (_lTimeIndex = lTimeIndex) || !org.drip.numerical.common.NumberUtil.IsValid (_dblX = dblX) ||
			!org.drip.numerical.common.NumberUtil.IsValid (_dblAlpha = dblAlpha))
			throw new java.lang.Exception ("TrinomialTreeNodeMetrics ctr: Invalid Inputs");

		_lXStochasticIndex = lXStochasticIndex;
	}

	/**
	 * Retrieve the Node's X
	 * 
	 * @return The Node's X
	 */

	public double x()
	{
		return _dblX;
	}

	/**
	 * Retrieve the Node's Alpha
	 * 
	 * @return The Node's Alpha
	 */

	public double alpha()
	{
		return _dblAlpha;
	}

	/**
	 * Retrieve the Node's Short Rate
	 * 
	 * @return The Node's Short Rate
	 */

	public double shortRate()
	{
		return _dblX + _dblAlpha;
	}

	/**
	 * Retrieve the Tree Node's Time Index
	 * 
	 * @return The Time Index
	 */

	public long timeIndex()
	{
		return _lTimeIndex;
	}

	/**
	 * Retrieve the Tree Node's X Stochastic Index
	 * 
	 * @return The Tree Node's X Stochastic Index
	 */

	public long xStochasticIndex()
	{
		return _lXStochasticIndex;
	}
}
