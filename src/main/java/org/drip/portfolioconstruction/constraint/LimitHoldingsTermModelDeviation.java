
package org.drip.portfolioconstruction.constraint;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2019 Lakshmi Krishnamurthy
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * Copyright (C) 2017 Lakshmi Krishnamurthy
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
 * <i>LimitHoldingsTermModelDeviation</i> holds the Details of a Limit Holdings Benchmark Weights Absolute
 * Deviation Constraint Term.
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/PortfolioCore.md">Portfolio Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/AssetAllocationAnalyticsLibrary.md">Asset Allocation Analytics Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/portfolioconstruction">Portfolio Construction</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/portfolioconstruction/constraint">Constraint</a></li>
 *  </ul>
 * <br><br>
 *
 * @author Lakshmi Krishnamurthy
 */

public class LimitHoldingsTermModelDeviation extends
	org.drip.portfolioconstruction.constraint.LimitHoldingsTerm
{
	private double[] _adblBenchmarkHoldings = null;

	/**
	 * LimitHoldingsTermModelDeviation Constructor
	 * 
	 * @param strName Name of the LimitHoldingsTermModelDeviation Constraint
	 * @param scope Scope of the LimitHoldingsTermModelDeviation Constraint
	 * @param unit Unit of the LimitHoldingsTermModelDeviation Constraint
	 * @param dblMinimum Minimum Value of the LimitHoldingsTermModelDeviation Constraint
	 * @param dblMaximum Maximum Value of the LimitHoldingsTermModelDeviation Constraint
	 * @param adblBenchmarkHoldings Array of the Constricted Benchmark Holdings
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid/Inconsistent
	 */

	public LimitHoldingsTermModelDeviation (
		final java.lang.String strName,
		final org.drip.portfolioconstruction.optimizer.Scope scope,
		final org.drip.portfolioconstruction.optimizer.Unit unit,
		final double dblMinimum,
		final double dblMaximum,
		final double[] adblBenchmarkHoldings)
		throws java.lang.Exception
	{
		super (
			strName,
			"CT_LIMIT_MODEL_DEVIATION",
			"Limit Holdings Model Deviation Constaint Term",
			scope,
			unit,
			dblMinimum,
			dblMaximum,
			null == adblBenchmarkHoldings ? 0 : adblBenchmarkHoldings.length
		);

		if (null == (_adblBenchmarkHoldings = adblBenchmarkHoldings) || 0 != _adblBenchmarkHoldings.length ||
			!org.drip.numerical.common.NumberUtil.IsValid (_adblBenchmarkHoldings))
			throw new java.lang.Exception
				("LimitHoldingsTermModelDeviation Constructor => Invalid Selection");
	}

	/**
	 * Retrieve the Array of Benchmark Constricted Holdings
	 * 
	 * @return Array of Benchmark Constricted Holdings
	 */

	public double[] benchmarkHoldings()
	{
		return _adblBenchmarkHoldings;
	}

	@Override public org.drip.function.definition.RdToR1 rdtoR1()
	{
		return new org.drip.function.definition.RdToR1 (null)
		{
			@Override public int dimension()
			{
				return size();
			}

			@Override public double evaluate (
				final double[] adblFinalHoldings)
				throws java.lang.Exception
			{
				double dblConstraintValue = 0.;
				int iNumAsset = _adblBenchmarkHoldings.length;

				if (null == adblFinalHoldings || !org.drip.numerical.common.NumberUtil.IsValid
					(adblFinalHoldings) || adblFinalHoldings.length != iNumAsset)
					throw new java.lang.Exception
						("LimitHoldingsTermModelDeviation::rdToR1::evaluate => Invalid Variate Dimension");

				for (int i = 0; i < iNumAsset; ++i)
					dblConstraintValue += java.lang.Math.abs (_adblBenchmarkHoldings[i] -
						adblFinalHoldings[i]);

				return dblConstraintValue;
			}
		};
	}
}
