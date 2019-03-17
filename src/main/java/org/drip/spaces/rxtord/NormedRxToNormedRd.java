
package org.drip.spaces.rxtord;

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
 * <i>NormedRxToNormedRd</i> is the Abstract Class that exposes f : Normed R<sup>x</sup> (x .gte. 1) To
 * Normed R<sup>d</sup> Function Space. The Reference we've used is:
 *
 * <br><br>
 *  <ul>
 *  	<li>
 *  		Carl, B., and I. Stephani (1990): <i>Entropy, Compactness, and the Approximation of Operators</i>
 *  			<b>Cambridge University Press</b> Cambridge UK 
 *  	</li>
 *  </ul>
 *
 * <br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/NumericalCore.md">Numerical Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/StatisticalLearningLibrary.md">Statistical Learning Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/spaces">Spaces</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/spaces/rxtord">R<sup>x</sup> To R<sup>d</sup></a></li>
 *  </ul>
 * <br><br>
 *
 * @author Lakshmi Krishnamurthy
 */

public abstract class NormedRxToNormedRd {

	/**
	 * Retrieve the Input Metric Vector Space
	 * 
	 * @return The Input Metric Vector Space
	 */

	public abstract org.drip.spaces.metric.GeneralizedMetricVectorSpace inputMetricVectorSpace();

	/**
	 * Retrieve the Output Metric Vector Space
	 * 
	 * @return The Output Metric Vector Space
	 */

	public abstract org.drip.spaces.metric.RdNormed outputMetricVectorSpace();

	/**
	 * Retrieve the Sample Supremum Norm Array
	 * 
	 * @param gvvi The Validated Vector Space Instance
	 * 
	 * @return The Sample Supremum Norm Array
	 */

	public abstract double[] sampleSupremumNorm (
		final org.drip.spaces.instance.GeneralizedValidatedVector gvvi);

	/**
	 * Retrieve the Sample Metric Norm Array
	 * 
	 * @param gvvi The Validated Vector Space Instance
	 * 
	 * @return The Sample Metric Norm Array
	 */

	public abstract double[] sampleMetricNorm (
		final org.drip.spaces.instance.GeneralizedValidatedVector gvvi);

	/**
	 * Retrieve the Sample Covering Number Array
	 * 
	 * @param gvvi The Validated Vector Space Instance
	 * @param dblCover The Cover
	 * 
	 * @return The Sample Covering Number Array
	 */

	public double[] sampleCoveringNumber (
		final org.drip.spaces.instance.GeneralizedValidatedVector gvvi,
		final double dblCover)
	{
		if (!org.drip.numerical.common.NumberUtil.IsValid (dblCover) || 0. >= dblCover) return null;

		double[] adblSampleMetricNorm = sampleMetricNorm (gvvi);

		if (null == adblSampleMetricNorm) return null;

		int iOutputDimensionality = adblSampleMetricNorm.length;
		double[] adblSampleCoveringNumber = new double[iOutputDimensionality];

		if (0 == iOutputDimensionality) return null;

		double dblCoverBallVolume = java.lang.Math.pow (dblCover, outputMetricVectorSpace().pNorm());

		for (int i = 0; i < iOutputDimensionality; ++i)
			adblSampleCoveringNumber[i] = adblSampleMetricNorm[i] / dblCoverBallVolume;

		return adblSampleCoveringNumber;
	}

	/**
	 * Retrieve the Sample Supremum Covering Number Array
	 * 
	 * @param gvvi The Validated Vector Space Instance
	 * @param dblCover The Cover
	 * 
	 * @return The Sample Supremum Covering Number Array
	 */

	public double[] sampleSupremumCoveringNumber (
		final org.drip.spaces.instance.GeneralizedValidatedVector gvvi,
		final double dblCover)
	{
		if (!org.drip.numerical.common.NumberUtil.IsValid (dblCover) || 0. >= dblCover) return null;

		double[] adblSampleSupremumNorm = sampleSupremumNorm (gvvi);

		if (null == adblSampleSupremumNorm) return null;

		int iOutputDimensionality = adblSampleSupremumNorm.length;
		double[] adblSampleSupremumCoveringNumber = new double[iOutputDimensionality];

		if (0 == iOutputDimensionality) return null;

		double dblCoverBallVolume = java.lang.Math.pow (dblCover, outputMetricVectorSpace().pNorm());

		for (int i = 0; i < iOutputDimensionality; ++i)
			adblSampleSupremumCoveringNumber[i] = adblSampleSupremumNorm[i] / dblCoverBallVolume;

		return adblSampleSupremumCoveringNumber;
	}

	/**
	 * Retrieve the Population ESS (Essential Spectrum) Array
	 * 
	 * @return The Population ESS (Essential Spectrum) Array
	 */

	public abstract double[] populationESS();

	/**
	 * Retrieve the Population Metric Norm Array
	 * 
	 * @return The Population Metric Norm Array
	 */

	public abstract double[] populationMetricNorm();

	/**
	 * Retrieve the Population Supremum Norm Array
	 * 
	 * @return The Population Supremum Norm Array
	 */

	public double[] populationSupremumNorm()
	{
		return populationMetricNorm();
	}

	/**
	 * Retrieve the Population Covering Number Array
	 * 
	 * @param dblCover The Cover
	 * 
	 * @return The Population Covering Number Array
	 */

	public double[] populationCoveringNumber (
		final double dblCover)
	{
		if (!org.drip.numerical.common.NumberUtil.IsValid (dblCover) || 0. >= dblCover) return null;

		double[] adblPopulationMetricNorm = populationMetricNorm();

		if (null == adblPopulationMetricNorm) return null;

		int iOutputDimensionality = adblPopulationMetricNorm.length;
		double[] adblPopulationCoveringNumber = new double[iOutputDimensionality];

		if (0 == iOutputDimensionality) return null;

		double dblCoverBallVolume = java.lang.Math.pow (dblCover, outputMetricVectorSpace().pNorm());

		for (int i = 0; i < iOutputDimensionality; ++i)
			adblPopulationCoveringNumber[i] = adblPopulationMetricNorm[i] / dblCoverBallVolume;

		return adblPopulationCoveringNumber;
	}

	/**
	 * Retrieve the Population Supremum Covering Number Array
	 * 
	 * @param dblCover The Cover
	 * 
	 * @return The Population Supremum Covering Number Array
	 */

	public double[] populationSupremumCoveringNumber (
		final double dblCover)
	{
		if (!org.drip.numerical.common.NumberUtil.IsValid (dblCover) || 0. >= dblCover) return null;

		double[] adblPopulationSupremumNorm = populationSupremumNorm();

		if (null == adblPopulationSupremumNorm) return null;

		int iOutputDimensionality = adblPopulationSupremumNorm.length;
		double[] adblPopulationSupremumCoveringNumber = new double[iOutputDimensionality];

		if (0 == iOutputDimensionality) return null;

		double dblCoverBallVolume = java.lang.Math.pow (dblCover, outputMetricVectorSpace().pNorm());

		for (int i = 0; i < iOutputDimensionality; ++i)
			adblPopulationSupremumCoveringNumber[i] = adblPopulationSupremumNorm[i] / dblCoverBallVolume;

		return adblPopulationSupremumCoveringNumber;
	}
}
