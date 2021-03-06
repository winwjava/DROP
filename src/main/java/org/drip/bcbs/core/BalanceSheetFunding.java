
package org.drip.bcbs.core;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2019 Lakshmi Krishnamurthy
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
 * <i>BalanceSheetFunding</i> holds the Quantities used to compute the Stable FUnding Ratios in the BCBS
 * Standards. The References are:
 *  
 * <br><br>
 * 	<ul>
 * 		<li>
 * 			Basel Committee on Banking Supervision (2017): Basel III Leverage Ratio Framework and Disclosure
 * 				Requirements https://www.bis.org/publ/bcbs270.pdf
 * 		</li>
 * 		<li>
 * 			Central Banking (2013): Fed and FDIC agree 6% Leverage Ratio for US SIFIs
 * 				https://www.centralbanking.com/central-banking/news/2280726/fed-and-fdic-agree-6-leverage-ratio-for-us-sifis
 * 		</li>
 * 		<li>
 * 			European Banking Agency (2013): Implementing Basel III in Europe: CRD IV Package
 * 				https://eba.europa.eu/regulation-and-policy/implementing-basel-iii-europe
 * 		</li>
 * 		<li>
 * 			Federal Reserve (2013): Liquidity Coverage Ratio � Liquidity Risk Measurements, Standards, and
 * 				Monitoring
 * 				https://web.archive.org/web/20131102074614/http:/www.federalreserve.gov/FR_notice_lcr_20131024.pdf
 * 		</li>
 * 		<li>
 * 			Wikipedia (2018): Basel III https://en.wikipedia.org/wiki/Basel_III
 * 		</li>
 * 	</ul>
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/AnalyticsCore.md">Analytics Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/ExposureAnalyticsLibrary.md">Exposure Analytics</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/bcbs/README.md">BCBS</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/bcbs/core/README.md">Core</a></li>
 *  </ul>
 * 
 * @author Lakshmi Krishnamurthy
 */

public class BalanceSheetFunding
{
	private java.lang.String _stressPeriod = "";
	private double _stableFundingAmount = java.lang.Double.NaN;
	private double _extendedStressFundingAmount = java.lang.Double.NaN;

	/**
	 * Construct the Basel III Version of BalanceSheetFunding
	 * 
	 * @param stableFundingAmount Stable Funding Amount
	 * @param extendedStressFundingAmount Funding Amount Required Over the Specified Period of Extended
	 * 		Stress
	 * 
	 * @return The Basel III Version of BalanceSheetFunding
	 */

	public static final BalanceSheetFunding Basel_III (
		final double stableFundingAmount,
		final double extendedStressFundingAmount)
	{
		try
		{
			return new BalanceSheetFunding (
				stableFundingAmount,
				extendedStressFundingAmount,
				"1Y"
			);
		}
		catch (java.lang.Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * BalanceSheetFunding Constructor
	 * 
	 * @param stableFundingAmount Stable Funding Amount
	 * @param extendedStressFundingAmount Funding Amount Required Over the Specified Period of Extended
	 * 		Stress
	 * @param stressPeriod Stress Period
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public BalanceSheetFunding (
		final double stableFundingAmount,
		final double extendedStressFundingAmount,
		final java.lang.String stressPeriod)
		throws java.lang.Exception
	{
		if (!org.drip.numerical.common.NumberUtil.IsValid (_stableFundingAmount = stableFundingAmount) ||
				0. > _stableFundingAmount ||
			!org.drip.numerical.common.NumberUtil.IsValid (_extendedStressFundingAmount =
				extendedStressFundingAmount) || 0. > _extendedStressFundingAmount ||
			null == (_stressPeriod = stressPeriod) || _stressPeriod.isEmpty())
		{
			throw new java.lang.Exception ("BalanceSheetFunding Constructor => Invalid Inputs");
		}
	}

	/**
	 * Retrieve the Stable Funding Amount
	 * 
	 * @return The Stable Funding Amount
	 */

	public double stableFundingAmount()
	{
		return _stableFundingAmount;
	}

	/**
	 * Retrieve the Funding Amount Required Over the Specified Period of Extended Stress
	 * 
	 * @return The Funding Amount Required Over the Specified Period of Extended Stress
	 */

	public double extendedStressFundingAmount()
	{
		return _extendedStressFundingAmount;
	}

	/**
	 * Retrieve the Stress Period
	 * 
	 * @return The Stress Period
	 */

	public java.lang.String stressPeriod()
	{
		return _stressPeriod;
	}

	/**
	 * Retrieve the Net Stable Funding Ratio
	 * 
	 * @return The Net Stable Funding Ratio
	 */

	public double netStableFundingRatio()
	{
		return _stableFundingAmount / _extendedStressFundingAmount;
	}
}
