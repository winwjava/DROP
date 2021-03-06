
package org.drip.sample.hullwhite;

import org.drip.analytics.date.*;
import org.drip.dynamics.hullwhite.*;
import org.drip.function.r1tor1.FlatUnivariate;
import org.drip.numerical.common.FormatUtil;
import org.drip.sequence.random.BoxMullerGaussian;
import org.drip.service.env.EnvManager;
import org.drip.state.identifier.FundingLabel;

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
 * <i>TrinomialTreeEvolution</i> demonstrates the Construction and Usage of the Hull-White Trinomial Tree and
 * the Eventual Evolution of the Short Rate on it.
 *  
 * <br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/AnalyticsCore.md">Analytics Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/FixedIncomeAnalyticsLibrary.md">Fixed Income Analytics Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/sample/README.md">Sample</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/sample/hullwhite/README.md">Hull-White 1F Dynamics</a></li>
 *  </ul>
 * <br><br>
 *
 * @author Lakshmi Krishnamurthy
 */

public class TrinomialTreeEvolution {

	private static final SingleFactorStateEvolver HullWhiteEvolver (
		final String strCurrency,
		final double dblSigma,
		final double dblA,
		final double dblStartingForwardRate)
		throws Exception
	{
		return new SingleFactorStateEvolver (
			FundingLabel.Standard (strCurrency),
			dblSigma,
			dblA,
			new FlatUnivariate (dblStartingForwardRate),
			new BoxMullerGaussian (
				0.,
				1.
			)
		);
	}

	private static final void DumpMetrics (
		final TrinomialTreeTransitionMetrics hwtm)
		throws Exception
	{
		System.out.println ("\t| [" + new JulianDate (hwtm.initialDate()) + " -> " +
			new JulianDate (hwtm.terminalDate()) + "] => " +
			FormatUtil.FormatDouble (hwtm.expectedTerminalX(), 1, 4, 1.) + " | " +
			FormatUtil.FormatDouble (Math.sqrt (hwtm.xVariance()), 1, 2, 100.) + "% | " +
			FormatUtil.FormatDouble (hwtm.xStochasticShift(), 1, 4, 1.) + " || " +
			FormatUtil.FormatDouble (hwtm.probabilityUp(), 1, 4, 1.) + " | " +
			FormatUtil.FormatDouble (hwtm.upNodeMetrics().x(), 1, 4, 1.) + " | " +
			FormatUtil.FormatDouble (hwtm.upNodeMetrics().shortRate(), 1, 2, 100.) + "% || " +
			FormatUtil.FormatDouble (hwtm.probabilityDown(), 1, 4, 1.) + " | " +
			FormatUtil.FormatDouble (hwtm.downNodeMetrics().x(), 1, 4, 1.) + " | " +
			FormatUtil.FormatDouble (hwtm.downNodeMetrics().shortRate(), 1, 2, 100.) + "% || " +
			FormatUtil.FormatDouble (hwtm.probabilityStay(), 1, 4, 1.) + " | " +
			FormatUtil.FormatDouble (hwtm.stayNodeMetrics().x(), 1, 4, 1.) + " | " +
			FormatUtil.FormatDouble (hwtm.stayNodeMetrics().shortRate(), 1, 2, 100.) + "% ||"
		);
	}

	private static final void TreeHeader (
		final String strEvolutionComment)
		throws Exception
	{
		System.out.println ("\n\n\t|----------------------------------------------------------------------------------------------------------------------------------------------------||");

		System.out.println ("\t|                                                                                                                                                    ||");

		System.out.println (strEvolutionComment);

		System.out.println ("\t|    ---------------------------------------------------                                                                                             ||");

		System.out.println ("\t|                                                                                                                                                    ||");

		System.out.println ("\t|    L->R:                                                                                                                                           ||");

		System.out.println ("\t|                                                                                                                                                    ||");

		System.out.println ("\t|        Initial Date                                                                                                                                ||");

		System.out.println ("\t|        Final Date                                                                                                                                  ||");

		System.out.println ("\t|        Expected Final X                                                                                                                            ||");

		System.out.println ("\t|        X Volatility (%)                                                                                                                            ||");

		System.out.println ("\t|        X Stochastic Shift                                                                                                                          ||");

		System.out.println ("\t|        Move-Up Probability                                                                                                                         ||");

		System.out.println ("\t|        Move-Up X Node Value                                                                                                                        ||");

		System.out.println ("\t|        Move-Up Short Rate                                                                                                                          ||");

		System.out.println ("\t|        Move-Down Probability                                                                                                                       ||");

		System.out.println ("\t|        Move-Down X Node Value                                                                                                                      ||");

		System.out.println ("\t|        Move-Down Short Rate                                                                                                                        ||");

		System.out.println ("\t|        Stay Probability                                                                                                                            ||");

		System.out.println ("\t|        Stay X Node Value                                                                                                                           ||");

		System.out.println ("\t|        Stay Short Rate                                                                                                                             ||");

		System.out.println ("\t|                                                                                                                                                    ||");

		System.out.println ("\t|----------------------------------------------------------------------------------------------------------------------------------------------------||");
	}

	public static final void main (
		final String[] astrArgs)
		throws Exception
	{
		EnvManager.InitEnv ("");

		JulianDate dtSpot = DateUtil.Today();

		String strCurrency = "USD";
		double dblStartingShortRate = 0.05;
		double dblSigma = 0.01;
		double dblA = 1.;

		SingleFactorStateEvolver hw = HullWhiteEvolver (
			strCurrency,
			dblSigma,
			dblA,
			dblStartingShortRate
		);

		int iSpotDate = dtSpot.julian();

		int iFinalDate = dtSpot.addMonths (6).julian();

		int iInitialDate = iSpotDate;
		TrinomialTreeTransitionMetrics hwtm = null;

		TreeHeader ("\t|    Hull-White Trinomial Tree Upwards Evolution Metrics                                                                                             ||");

		while (iInitialDate < iFinalDate) {
			DumpMetrics (hwtm =
				hw.evolveTrinomialTree (
					iSpotDate,
					iInitialDate,
					iFinalDate,
					null == hwtm ? null : hwtm.upNodeMetrics()
				)
			);

			iInitialDate += 10;
		}

		System.out.println ("\t|----------------------------------------------------------------------------------------------------------------------------------------------------||");

		hwtm = null;
		iInitialDate = iSpotDate;

		TreeHeader ("\t|    Hull-White Trinomial Tree Downwards Evolution Metrics                                                                                           ||");

		while (iInitialDate < iFinalDate) {
			DumpMetrics (hwtm =
				hw.evolveTrinomialTree (
					iSpotDate,
					iInitialDate,
					iFinalDate,
					null == hwtm ? null : hwtm.downNodeMetrics()
				)
			);

			iInitialDate += 10;
		}

		System.out.println ("\t|----------------------------------------------------------------------------------------------------------------------------------------------------||");

		hwtm = null;
		iInitialDate = iSpotDate;

		TreeHeader ("\t|    Hull-White Trinomial Tree Stay-Put Evolution Metrics                                                                                            ||");

		while (iInitialDate < iFinalDate) {
			DumpMetrics (hwtm =
				hw.evolveTrinomialTree (
					iSpotDate,
					iInitialDate,
					iFinalDate,
					null == hwtm ? null : hwtm.stayNodeMetrics()
				)
			);

			iInitialDate += 10;
		}

		System.out.println ("\t|----------------------------------------------------------------------------------------------------------------------------------------------------||");

		EnvManager.TerminateEnv();
	}
}
