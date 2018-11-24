
package org.drip.state.csa;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2019 Lakshmi Krishnamurthy
 * Copyright (C) 2018 Lakshmi Krishnamurthy
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
 * <i>CashFlowEstimator</i> estimates the Cash Flow Rate to be applied between the specified Dates.
 *
 *  <br><br>
 *  <ul>
 *		<li><b>Project</b>       = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/state">State</a></li>
 *		<li><b>Package</b>       = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/state/csa">CSA</a></li>
 *		<li><b>Specification</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/Docs/Internal/FixedIncome">Fixed Income Analytics Library</a></li>
 *  </ul>
 * <br><br>
 *
 * @author Lakshmi Krishnamurthy
 */

public interface CashFlowEstimator extends org.drip.state.discount.DiscountFactorEstimator
{

	/**
	 * Calculate the Cash Flow Rate Effective to the given Date
	 * 
	 * @param iDate Date
	 * 
	 * @return The Cash Flow Rate
	 * 
	 * @throws java.lang.Exception Thrown if the Cash Flow Rate cannot be calculated
	 */

	public abstract double rate (
		final int iDate)
		throws java.lang.Exception;

	/**
	 * Calculate the Cash Flow Rate Effective to the given date
	 * 
	 * @param dt Date
	 * 
	 * @return The Cash Flow Rate
	 * 
	 * @throws java.lang.Exception Thrown if the Cash Flow Rate cannot be Calculated
	 */

	public abstract double rate (
		final org.drip.analytics.date.JulianDate dt)
		throws java.lang.Exception;

	/**
	 * Calculate the Cash Flow Rate Effective to the given Tenor
	 * 
	 * @param strTenor Tenor
	 * 
	 * @return The Cash Flow Rate
	 * 
	 * @throws java.lang.Exception Thrown if the Cash Flow Rate cannot be calculated
	 */

	public abstract double rate (
		final java.lang.String strTenor)
		throws java.lang.Exception;

	/**
	 * Calculate the Cash Flow Rate Effective between the Tenors
	 * 
	 * @param strTenor1 Tenor #1
	 * @param strTenor2 Tenor #2
	 * 
	 * @return The Cash Flow Rate
	 * 
	 * @throws java.lang.Exception Thrown if the Cash Flow Rate cannot be calculated
	 */

	public abstract double rate (
		final java.lang.String strTenor1,
		final java.lang.String strTenor2)
		throws java.lang.Exception;

	/**
	 * Calculate the Cash Flow Rate Effective between the Dates
	 * 
	 * @param iDate1 Date #1
	 * @param iDate2 Date #2
	 * 
	 * @return The Cash Flow Rate
	 * 
	 * @throws java.lang.Exception Thrown if the Cash Flow Rate cannot be calculated
	 */

	public abstract double rate (
		final int iDate1,
		final int iDate2)
		throws java.lang.Exception;

	/**
	 * Calculate the Cash Flow Rate Effective between the Dates
	 * 
	 * @param dt1 Date #1
	 * @param dt2 Date #2
	 * 
	 * @return The Cash Flow Rate
	 * 
	 * @throws java.lang.Exception Thrown if the Cash Flow Rate cannot be calculated
	 */

	public abstract double rate (
		final org.drip.analytics.date.JulianDate dt1,
		final org.drip.analytics.date.JulianDate dt2)
		throws java.lang.Exception;
}
