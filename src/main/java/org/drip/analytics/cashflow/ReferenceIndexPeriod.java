
package org.drip.analytics.cashflow;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2019 Lakshmi Krishnamurthy
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * Copyright (C) 2017 Lakshmi Krishnamurthy
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
 * <i>ReferenceIndexPeriod</i> contains the Cash Flow Period Details. Currently it holds the Start Date, the
 * End Date, the Fixing Date, and the Reference Latent State Label.
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/AnalyticsCore.md">Analytics Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/FixedIncomeAnalyticsLibrary.md">Fixed Income Analytics</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/analytics/README.md">Analytics</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/analytics/cashflow/README.md">Cash Flow</a></li>
 *  </ul>
 *
 * @author Lakshmi Krishnamurthy
 */

public class ReferenceIndexPeriod
{
	private double _dblDCF = java.lang.Double.NaN;
	private int _iEndDate = java.lang.Integer.MIN_VALUE;
	private int _iStartDate = java.lang.Integer.MIN_VALUE;
	private int _iFixingDate = java.lang.Integer.MIN_VALUE;
	private org.drip.state.identifier.FloaterLabel _floaterLabel = null;

	/**
	 * Standard Instance of ReferenceIndexPeriod
	 * 
	 * @param iStartDate Reference Period Start Date
	 * @param iEndDate Reference Period End Date
	 * @param floaterLabel Period Forward Label
	 * 
	 * @return The ReferenceIndexPeriod Instance
	 */

	public static final ReferenceIndexPeriod Standard (
		final int iStartDate,
		final int iEndDate,
		final org.drip.state.identifier.FloaterLabel floaterLabel)
	{
		if (null == floaterLabel) return null;

		org.drip.analytics.daycount.DateAdjustParams dapFixing =
			floaterLabel.floaterIndex().spotLagDAPBackward();

		org.drip.param.period.UnitCouponAccrualSetting ucas = floaterLabel.ucas();

		try {
			return new ReferenceIndexPeriod (
				iStartDate,
				iEndDate,
				null == dapFixing ? iStartDate : dapFixing.roll (iStartDate),
				ucas.couponDCFOffOfFreq() ? 1. / ucas.freq() :
					org.drip.analytics.daycount.Convention.YearFraction (
						iStartDate,
						iEndDate,
						ucas.couponDC(),
						ucas.couponEOMAdjustment(),
						null,
						ucas.calendar()
					),
				floaterLabel
			);
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * The ReferenceIndexPeriod Constructor
	 * 
	 * @param iStartDate Reference Period Start Date
	 * @param iEndDate Reference Period End Date
	 * @param iFixingDate Reference Period Fixing Date
	 * @param dblDCF Reference Period Day Count Fraction
	 * @param floaterLabel Period Floater Label
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public ReferenceIndexPeriod (
		final int iStartDate,
		final int iEndDate,
		final int iFixingDate,
		final double dblDCF,
		final org.drip.state.identifier.FloaterLabel floaterLabel)
		throws java.lang.Exception
	{
		if ((_iEndDate = iEndDate) <= (_iStartDate = iStartDate) ||
			(_iFixingDate = iFixingDate) > _iStartDate ||
			!org.drip.numerical.common.NumberUtil.IsValid (_dblDCF = dblDCF) ||
			null == (_floaterLabel = floaterLabel))
			throw new java.lang.Exception ("ReferenceIndexPeriod ctr: Invalid Inputs");
	}

	/**
	 * Reference Period Start Date
	 * 
	 * @return The Reference Period Start Date
	 */

	public int startDate()
	{
		return _iStartDate;
	}

	/**
	 * Reference Period End Date
	 * 
	 * @return The Reference Period End Date
	 */

	public int endDate()
	{
		return _iEndDate;
	}

	/**
	 * Reference Period Fixing Date
	 * 
	 * @return The Reference Period Fixing Date
	 */

	public int fixingDate()
	{
		return _iFixingDate;
	}

	/**
	 * Retrieve the Floater Label
	 * 
	 * @return The Floater Label
	 */

	public org.drip.state.identifier.FloaterLabel floaterLabel()
	{
		return _floaterLabel;
	}

	/**
	 * Retrieve the Reference Period Day Count Fraction
	 * 
	 * @return The Reference Period Day Count Fraction
	 */

	public double dcf()
	{
		return _dblDCF;
	}
}
