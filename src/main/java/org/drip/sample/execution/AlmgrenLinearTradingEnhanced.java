
package org.drip.sample.execution;

import org.drip.execution.capture.TrajectoryShortfallEstimator;
import org.drip.execution.dynamics.*;
import org.drip.execution.impact.ParticipationRateLinear;
import org.drip.execution.nonadaptive.*;
import org.drip.execution.optimum.*;
import org.drip.execution.profiletime.UniformParticipationRateLinear;
import org.drip.execution.risk.MeanVarianceObjectiveUtility;
import org.drip.execution.strategy.*;
import org.drip.measure.gaussian.R1UnivariateNormal;
import org.drip.quant.common.FormatUtil;
import org.drip.service.env.EnvManager;

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
 * <i>AlmgrenLinearTradingEnhanced</i> demonstrates the Generation of the Optimal Trading Trajectory under
 * the Condition of Linear Trading Enhanced Volatility using a Numerical Optimization Technique. The
 * References are:
 * 
 * <br><br>
 *  <ul>
 *  	<li>
 * 			Almgren, R., and N. Chriss (1999): Value under Liquidation <i>Risk</i> <b>12 (12)</b>
 *  	</li>
 *  	<li>
 * 			Almgren, R., and N. Chriss (2000): Optimal Execution of Portfolio Transactions <i>Journal of
 * 				Risk</i> <b>3 (2)</b> 5-39
 *  	</li>
 *  	<li>
 * 			Almgren, R. (2003): Optimal Execution with Nonlinear Impact Functions and Trading-Enhanced Risk
 * 				<i>Applied Mathematical Finance</i> <b>10 (1)</b> 1-18
 *  	</li>
 *  	<li>
 * 			Almgren, R., and N. Chriss (2003): Bidding Principles <i>Risk</i> 97-102
 *  	</li>
 *  	<li>
 * 			Bertsimas, D., and A. W. Lo (1998): Optimal Control of Execution Costs <i>Journal of Financial
 * 				Markets</i> <b>1</b> 1-50
 *  	</li>
 *  </ul>
 * 
 * <br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/PortfolioCore.md">Portfolio Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/TransactionCostAnalyticsLibrary.md">Transaction Cost Analytics Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/sample/README.md">Sample</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/sample/execution/README.md">Execution</a></li>
 *  </ul>
 * <br><br>
 * 
 * @author Lakshmi Krishnamurthy
 */

public class AlmgrenLinearTradingEnhanced {

	public static final void main (
		final String[] astrArgs)
		throws Exception
	{
		EnvManager.InitEnv ("");

		double dblEta = 5.e-06;
		double dblBeta = 2.e-06;
		double dblSigma = 1.;
		double dblLambda = 1.e-05;

		double dblX = 100000.;
		double dblT = 5.;
		int iNumInterval = 20;

		DiscreteTradingTrajectoryControl dttc = DiscreteTradingTrajectoryControl.FixedInterval (
			new OrderSpecification (
				dblX,
				dblT
			),
			iNumInterval
		);

		ArithmeticPriceEvolutionParameters apep = ArithmeticPriceEvolutionParametersBuilder.TradingEnhancedVolatility (
			dblSigma,
			new UniformParticipationRateLinear (ParticipationRateLinear.SlopeOnly (dblEta)),
			new UniformParticipationRateLinear (
				new ParticipationRateLinear (
					0.,
					dblBeta
				)
			)
		);

		EfficientTradingTrajectoryDiscrete ettd = (EfficientTradingTrajectoryDiscrete) new StaticOptimalSchemeDiscrete (
			dttc,
			apep,
			new MeanVarianceObjectiveUtility (dblLambda)
		).generate();

		double[] adblExecutionTimeNode = ettd.executionTimeNode();

		double[] adblTradeList = ettd.tradeList();

		double[] adblHoldings = ettd.holdings();

		DiscreteLinearTradingEnhanced dlte = DiscreteLinearTradingEnhanced.Standard (
			dblX,
			dblT,
			iNumInterval,
			apep,
			dblLambda
		);

		TradingEnhancedDiscrete ted = (TradingEnhancedDiscrete) dlte.generate();

		double[] adblTradeListCF = ted.tradeList();

		double[] adblHoldingsCF = ted.holdings();

		TrajectoryShortfallEstimator tse = new TrajectoryShortfallEstimator (ettd);

		R1UnivariateNormal r1un = tse.totalCostDistributionSynopsis (apep);

		System.out.println ("\n\t|------------------------------------------------||");

		System.out.println ("\t| NUMERICAL - CLOSED FORM CONTINUOUS TRAJECTORY  ||");

		System.out.println ("\t|------------------------------------------------||");

		System.out.println ("\t|    L -> R:                                     ||");

		System.out.println ("\t|          - Execution Time Node                 ||");

		System.out.println ("\t|          - Holdings (Numerical)                ||");

		System.out.println ("\t|          - Holdings (Continuous Closed Form)   ||");

		System.out.println ("\t|          - Trade List (Numerical)              ||");

		System.out.println ("\t|          - Trade List (Continuous Closed Form) ||");

		System.out.println ("\t|------------------------------------------------||");

		for (int i = 1; i < adblExecutionTimeNode.length; ++i) {
			System.out.println ("\t| " +
				FormatUtil.FormatDouble (adblExecutionTimeNode[i], 1, 2, 1.) + " => " +
				FormatUtil.FormatDouble (adblHoldings[i] / dblX, 2, 2, 100.) + "% | " +
				FormatUtil.FormatDouble (adblHoldingsCF[i] / dblX, 2, 2, 100.) + "% | " +
				FormatUtil.FormatDouble (adblTradeList[i - 1] / dblX, 2, 2, 100.) + "% | " +
				FormatUtil.FormatDouble (adblTradeListCF[i - 1] / dblX, 2, 2, 100.) + "% ||"
			);
		}

		System.out.println ("\t|------------------------------------------------||");

		System.out.println ("\n\t|--------------------------------------------------------------------------||");

		System.out.println ("\t|  TRANSACTION COST RECONCILIATION: EXPLICIT vs. NUMERICAL vs. CLOSED FORM ||");

		System.out.println ("\t|--------------------------------------------------------------------------||");

		System.out.println (
			"\t| Transaction Cost Expectation         : " +
			FormatUtil.FormatDouble (r1un.mean(), 6, 1, 1.) + " | " +
			FormatUtil.FormatDouble (ettd.transactionCostExpectation(), 6, 1, 1.) + " | " +
			FormatUtil.FormatDouble (ted.transactionCostExpectation(), 6, 1, 1.) + " ||"
		);

		System.out.println (
			"\t| Transaction Cost Variance (X 10^-06) : " +
			FormatUtil.FormatDouble (r1un.variance(), 6, 1, 1.e-06) + " | " +
			FormatUtil.FormatDouble (ettd.transactionCostVariance(), 6, 1, 1.e-06) + " | " +
			FormatUtil.FormatDouble (ted.transactionCostVariance(), 6, 1, 1.e-06) + " ||"
		);

		System.out.println ("\t|--------------------------------------------------------------------------||");

		EnvManager.TerminateEnv();
	}
}
