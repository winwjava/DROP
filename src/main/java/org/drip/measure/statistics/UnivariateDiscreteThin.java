
package org.drip.measure.statistics;

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
 * <i>UnivariateDiscreteThin</i> analyzes and computes the "Thin" Statistics for the Realized Univariate
 * Sequence.
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/NumericalCore.md">Numerical Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/NumericalOptimizerLibrary.md">Numerical Optimizer Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/measure">Measure</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/measure/statistics">Statistics</a></li>
 *  </ul>
 *
 * @author Lakshmi Krishnamurthy
 */

public class UnivariateDiscreteThin {
	private double _dblError = java.lang.Double.NaN;
	private double _dblAverage = java.lang.Double.NaN;
	private double _dblMaximum = java.lang.Double.NaN;
	private double _dblMinimum = java.lang.Double.NaN;

	/**
	 * Generate a UnivariateDiscreteThin Instance from the specified List of Double's
	 * 
	 * @param doubleList The List of Doubles
	 * 
	 * @return The UnivariateDiscreteThin Instance
	 */

	public static final UnivariateDiscreteThin FromList (
		final java.util.List<java.lang.Double> doubleList)
	{
		if (null == doubleList)
		{
			return null;
		}

		int listSize = doubleList.size();

		if (0 == listSize)
		{
			return null;
		}

		double[] sequence = new double[listSize];

		for (int index = 0; index < listSize; ++index)
		{
			sequence[index] = doubleList.get (index);
		}

		try
		{
			return new UnivariateDiscreteThin (sequence);
		}
		catch (java.lang.Exception e)
		{
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * UnivariateDiscreteThin Constructor
	 * 
	 * @param adblSequence The Univariate Sequence
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public UnivariateDiscreteThin (
		final double[] adblSequence)
		throws java.lang.Exception
	{
		if (null == adblSequence)
			throw new java.lang.Exception ("UnivariateDiscreteThin Constructor => Invalid Inputs");

		_dblError = 0.;
		_dblAverage = 0.;
		_dblMaximum = 0.;
		_dblMinimum = 0.;
		int iSequenceSize = adblSequence.length;

		if (0 == iSequenceSize)
			throw new java.lang.Exception ("UnivariateDiscreteThin Constructor => Invalid Inputs");

		for (int i = 0; i < iSequenceSize; ++i) {
			if (!org.drip.numerical.common.NumberUtil.IsValid (adblSequence[i]))
				throw new java.lang.Exception ("UnivariateDiscreteThin Constructor => Invalid Inputs");

			if (0 == i) {
				_dblMaximum = adblSequence[0];
				_dblMinimum = adblSequence[0];
			} else {
				if (_dblMaximum < adblSequence[i]) _dblMaximum = adblSequence[i];

				if (_dblMinimum > adblSequence[i]) _dblMinimum = adblSequence[i];
			}

			_dblAverage = _dblAverage + adblSequence[i];
		}

		_dblAverage /= iSequenceSize;

		for (int i = 0; i < iSequenceSize; ++i)
			_dblError = _dblError + java.lang.Math.abs (_dblAverage - adblSequence[i]);

		_dblError /= iSequenceSize;
	}

	/**
	 * Retrieve the Sequence Average
	 * 
	 * @return The Sequence Average
	 */

	public double average()
	{
		return _dblAverage;
	}

	/**
	 * Retrieve the Sequence Error
	 * 
	 * @return The Sequence Error
	 */

	public double error()
	{
		return _dblError;
	}

	/**
	 * Retrieve the Sequence Maximum
	 * 
	 * @return The Sequence Maximum
	 */

	public double maximum()
	{
		return _dblMaximum;
	}

	/**
	 * Retrieve the Sequence Minimum
	 * 
	 * @return The Sequence Minimum
	 */

	public double minimum()
	{
		return _dblMinimum;
	}
}
