
package org.drip.execution.principal;

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
 * <i>OptimalMeasureDependence</i> contains the Dependence Exponents on Liquidity, Trade Size, and Permanent
 * Impact Adjusted Principal Discount for the Optimal Principal Horizon and the Optional Information Ratio.
 * It also holds the Constant. The References are:
 * 
 * <br><br>
 * 	<ul>
 * 	<li>
 * 		Almgren, R., and N. Chriss (1999): Value under Liquidation <i>Risk</i> <b>12 (12)</b>
 * 	</li>
 * 	<li>
 * 		Almgren, R., and N. Chriss (2000): Optimal Execution of Portfolio Transactions <i>Journal of Risk</i>
 * 			<b>3 (2)</b> 5-39
 * 	</li>
 * 	<li>
 * 		Almgren, R. (2003): Optimal Execution with Nonlinear Impact Functions and Trading-Enhanced Risk
 * 			<i>Applied Mathematical Finance</i> <b>10 (1)</b> 1-18
 * 	</li>
 * 	<li>
 * 		Almgren, R., and N. Chriss (2003): Bidding Principles <i>Risk</i> 97-102
 * 	</li>
 * 	<li>
 * 		Almgren, R., C. Thum, E. Hauptmann, and H. Li (2005): Equity Market Impact <i>Risk</i> <b>18 (7)</b>
 * 			57-62
 * 	</li>
 * 	</ul>
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/PortfolioCore.md">Portfolio Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/TransactionCostAnalyticsLibrary.md">Transaction Cost Analytics</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/execution/README.md">Execution</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/execution/principal/README.md">Principal</a></li>
 *  </ul>
 * 
 * @author Lakshmi Krishnamurthy
 */

public class OptimalMeasureDependence {
	private double _dblConstant = java.lang.Double.NaN;
	private double _dblBlockSizeExponent = java.lang.Double.NaN;
	private double _dblLiquidityExponent = java.lang.Double.NaN;
	private double _dblVolatilityExponent = java.lang.Double.NaN;
	private double _dblAdjustedPrincipalDiscountExponent = java.lang.Double.NaN;

	/**
	 * OptimalMeasureDependence Constructor
	 * 
	 * @param dblConstant The Optimal Measure Constant
	 * @param dblLiquidityExponent The Optimal Measure Liquidity Exponent
	 * @param dblBlockSizeExponent The Optimal Measure Block Size Exponent
	 * @param dblVolatilityExponent The Optimal Measure Volatility Exponent
	 * @param dblAdjustedPrincipalDiscountExponent The Optimal Measure Adjusted Principal Discount Exponent
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public OptimalMeasureDependence (
		final double dblConstant,
		final double dblLiquidityExponent,
		final double dblBlockSizeExponent,
		final double dblVolatilityExponent,
		final double dblAdjustedPrincipalDiscountExponent)
		throws java.lang.Exception
	{
		if (!org.drip.numerical.common.NumberUtil.IsValid (_dblConstant = dblConstant) ||
			!org.drip.numerical.common.NumberUtil.IsValid (_dblLiquidityExponent = dblLiquidityExponent) ||
				!org.drip.numerical.common.NumberUtil.IsValid (_dblBlockSizeExponent = dblBlockSizeExponent) ||
					!org.drip.numerical.common.NumberUtil.IsValid (_dblVolatilityExponent =
						dblVolatilityExponent) || !org.drip.numerical.common.NumberUtil.IsValid
							(_dblAdjustedPrincipalDiscountExponent = dblAdjustedPrincipalDiscountExponent))
			throw new java.lang.Exception ("OptimalMeasureDependence Constructor => Invalid Inputs!");
	}

	/**
	 * Retrieve the Constant
	 * 
	 * @return The Constant
	 */

	public double constant()
	{
		return _dblConstant;
	}

	/**
	 * Retrieve the Block Size Dependence Exponent
	 * 
	 * @return The Block Size Dependence Exponent
	 */

	public double blockSizeExponent()
	{
		return _dblBlockSizeExponent;
	}

	/**
	 * Retrieve the Liquidity Dependence Exponent
	 * 
	 * @return The Liquidity Dependence Exponent
	 */

	public double liquidityExponent()
	{
		return _dblLiquidityExponent;
	}

	/**
	 * Retrieve the Volatility Dependence Exponent
	 * 
	 * @return The Volatility Dependence Exponent
	 */

	public double volatilityExponent()
	{
		return _dblVolatilityExponent;
	}

	/**
	 * Retrieve the Adjusted Principal Discount Dependence Exponent
	 * 
	 * @return The Adjusted Principal Discount Dependence Exponent
	 */

	public double adjustedPrincipalDiscountExponent()
	{
		return _dblAdjustedPrincipalDiscountExponent;
	}
}
