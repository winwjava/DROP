
package org.drip.portfolioconstruction.objective;

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
 * <i>TransactionChargeTerm</i> implements the Objective Term that models the Charge associated with a
 * Portfolio Transaction.
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/PortfolioCore.md">Portfolio Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/AssetAllocationAnalyticsLibrary.md">Asset Allocation Analytics Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/portfolioconstruction">Portfolio Construction</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/portfolioconstruction/objective">Objective</a></li>
 *  </ul>
 * <br><br>
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class TransactionChargeTerm extends org.drip.portfolioconstruction.optimizer.ObjectiveTerm
{
	private org.drip.portfolioconstruction.cost.TransactionCharge[] _aTransactionCharge = null;

	protected TransactionChargeTerm (
		final java.lang.String strName,
		final java.lang.String strID,
		final java.lang.String strDescription,
		final double[] adblInitialHoldings,
		final org.drip.portfolioconstruction.cost.TransactionCharge[] aTransactionCharge)
		throws java.lang.Exception
	{
		super (
			strName,
			strID,
			strDescription,
			"TRANSACTION_COST",
			adblInitialHoldings
		);

		int iNumAsset = adblInitialHoldings.length;

		if (null == (_aTransactionCharge = aTransactionCharge) || iNumAsset != _aTransactionCharge.length)
			throw new java.lang.Exception ("TransactionChargeTerm Constructor => Invalid Inputs");

		for (int i = 0; i < iNumAsset; ++i)
		{
			if (null == _aTransactionCharge[i])
				throw new java.lang.Exception ("TransactionChargeTerm Constructor => Invalid Inputs");
		}
	}

	/**
	 * Retrieve the Array of Transaction Charges
	 * 
	 * @return The Transaction Charge Array
	 */

	public org.drip.portfolioconstruction.cost.TransactionCharge[] transactionCharge()
	{
		return _aTransactionCharge;
	}

	@Override public org.drip.function.definition.RdToR1 rdtoR1()
	{
		return new org.drip.function.definition.RdToR1 (null)
		{
			@Override public int dimension()
			{
				return initialHoldings().length;
			}

			@Override public double evaluate (
				final double[] adblVariate)
				throws java.lang.Exception
			{
				if (null == adblVariate || !org.drip.numerical.common.NumberUtil.IsValid (adblVariate))
					throw new java.lang.Exception
						("TransactionChargeTerm::rdToR1::evaluate => Invalid Input");

				org.drip.portfolioconstruction.cost.TransactionCharge[] aTransactionCharge =
					transactionCharge();

				double[] adblInitialHoldings = initialHoldings();

				int iNumAsset = aTransactionCharge.length;
				double dblFixedChargeTerm = 0.;

				if (adblVariate.length != iNumAsset)
					throw new java.lang.Exception
						("TransactionChargeTerm::rdToR1::evaluate => Invalid Variate Dimension");

				for (int i = 0; i < iNumAsset; ++i)
					dblFixedChargeTerm += aTransactionCharge[i].estimate (
						adblInitialHoldings[i],
						adblVariate[i]
					);

				return dblFixedChargeTerm;
			}
		};
	}
}
