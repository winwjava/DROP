
package org.drip.execution.adaptive;

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
 * <i>CoordinatedVariationRollingHorizon</i> implements the "Rolling Horizon" Approximation of the Optimal
 *  Cost Dynamic Trajectory arising from the Coordinated Variation Version of the Stochastic Volatility and
 *  the Transaction Function arising from the Realization of the Market State Variable as described in the
 *  "Trading Time" Model. The References are:
 * 	<br>
 *  <ul>
 * 		<li>
 * 			Almgren, R. F., and N. Chriss (2000): Optimal Execution of Portfolio Transactions <i>Journal of
 * 				Risk</i> <b>3 (2)</b> 5-39
 * 		</li>
 * 		<li>
 * 			Almgren, R. F. (2009): Optimal Trading in a Dynamic Market
 * 				https://www.math.nyu.edu/financial_mathematics/content/02_financial/2009-2.pdf
 * 		</li>
 * 		<li>
 * 			Almgren, R. F. (2012): Optimal Trading with Stochastic Liquidity and Volatility <i>SIAM Journal
 * 			of Financial Mathematics</i> <b>3 (1)</b> 163-181
 * 		</li>
 * 		<li>
 * 			Geman, H., D. B. Madan, and M. Yor (2001): Time Changes for Levy Processes <i>Mathematical
 * 				Finance</i> <b>11 (1)</b> 79-96
 * 		</li>
 * 		<li>
 * 			Jones, C. M., G. Kaul, and M. L. Lipson (1994): Transactions, Volume, and Volatility <i>Review of
 * 				Financial Studies</i> <b>7 (4)</b> 631-651
 * 		</li>
 *  </ul>
 * 	<br>
 *  <ul>
 *		<li><b>Module</b>        = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/execution">Execution</a></li>
 *		<li><b>Package</b>       = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/execution/adaptive">Adaptive</a></li>
 *		<li><b>Specification</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/Docs/Internal/TransactionCost">Transaction Cost Analytics</a></li>
 *  </ul>
 * 
 * @author Lakshmi Krishnamurthy
 */

public class CoordinatedVariationRollingHorizon extends
	org.drip.execution.adaptive.CoordinatedVariationTrajectory {
	private double[] _adblNonDimensionalCost = null;
	private double[] _adblNonDimensionalHoldings = null;
	private double[] _adblNonDimensionalTradeRate = null;

	/**
	 * CoordinatedVariationRollingHorizon Constructor
	 * 
	 * @param cvtd The Coordinated Variation Trajectory Determinant 
	 * @param adblNonDimensionalHoldings The Array of the Non Dimensional Holdings
	 * @param adblNonDimensionalTradeRate The Array of the Non Dimensional Trade Rate
	 * @param adblNonDimensionalCost The Array of the Non Dimensional Cost
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public CoordinatedVariationRollingHorizon (
		final org.drip.execution.adaptive.CoordinatedVariationTrajectoryDeterminant cvtd,
		final double[] adblNonDimensionalHoldings,
		final double[] adblNonDimensionalTradeRate,
		final double[] adblNonDimensionalCost)
		throws java.lang.Exception
	{
		super (cvtd);

		if (null == (_adblNonDimensionalHoldings = adblNonDimensionalHoldings) || null ==
			(_adblNonDimensionalTradeRate = adblNonDimensionalTradeRate) || null == (_adblNonDimensionalCost
				= adblNonDimensionalCost) || !org.drip.quant.common.NumberUtil.IsValid
					(_adblNonDimensionalHoldings) || !org.drip.quant.common.NumberUtil.IsValid
						(_adblNonDimensionalTradeRate) || !org.drip.quant.common.NumberUtil.IsValid
							(_adblNonDimensionalCost))
			throw new java.lang.Exception
				("CoordinatedVariationRollingHorizon Constructor => Invalid Inputs");

		int iNumTimeNode = _adblNonDimensionalHoldings.length;

		if (0 == iNumTimeNode || iNumTimeNode != _adblNonDimensionalTradeRate.length)
			throw new java.lang.Exception
				("CoordinatedVariationRollingHorizon Constructor => Invalid Inputs");
	}

	/**
	 * Retrieve the Array of the Non Dimensional Holdings
	 * 
	 * @return The Array of the Non Dimensional Holdings
	 */

	public double[] nonDimensionalHoldings()
	{
		return _adblNonDimensionalHoldings;
	}

	/**
	 * Retrieve the Array of the Non Dimensional Trade Rate
	 * 
	 * @return The Array of the Non Dimensional Trade Rate
	 */

	public double[] nonDimensionalTradeRate()
	{
		return _adblNonDimensionalTradeRate;
	}

	/**
	 * Retrieve the Array of the Non Dimensional Cost
	 * 
	 * @return The Array of the Non Dimensional Cost
	 */

	public double[] nonDimensionalCost()
	{
		return _adblNonDimensionalCost;
	}
}
