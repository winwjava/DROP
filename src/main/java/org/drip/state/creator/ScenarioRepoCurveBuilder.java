
package org.drip.state.creator;

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
 * <i>ScenarioRepoCurveBuilder</i> implements the Construction of the Scenario Repo Curve using the Input
 * Instruments and their Quotes.
 *
 *  <br><br>
 *  <ul>
 *		<li><b>Project</b>       = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/state">State</a></li>
 *		<li><b>Package</b>       = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/state/creator">Creator</a></li>
 *		<li><b>Specification</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/Docs/Internal/FixedIncome">Fixed Income Analytics Library</a></li>
 *  </ul>
 * <br><br>
 * 
 * @author Lakshmi Krishnamurthy
 */

public class ScenarioRepoCurveBuilder {

	/**
	 * Create an Instance of the Custom Splined Repo Curve
	 * 
	 * @param strName Curve Name
	 * @param dtSpot The Spot Date
	 * @param comp The Underlying Repo Component
	 * @param aiDate Array of the Dates
	 * @param adblRepo Array of the Repo Rates
	 * @param scbc The Segment Custom Builder Control
	 * 
	 * @return The Instance of the Custom Splined Repo Curve
	 */

	public static final org.drip.state.repo.RepoCurve CustomSplineRepoCurve (
		final java.lang.String strName,
		final org.drip.analytics.date.JulianDate dtSpot,
		final org.drip.product.definition.Component comp,
		final int[] aiDate,
		final double[] adblRepo,
		final org.drip.spline.params.SegmentCustomBuilderControl scbc)
	{
		if (null == strName || null == dtSpot || strName.isEmpty() || null == aiDate || null == adblRepo)
			return null;

		int iNumInstrument = aiDate.length;
		int[] aiBasisPredictorOrdinate = new int[iNumInstrument + 1];
		double[] adblBasisResponseValue = new double[iNumInstrument + 1];
		org.drip.spline.params.SegmentCustomBuilderControl[] aSCBC = new
			org.drip.spline.params.SegmentCustomBuilderControl[iNumInstrument];

		if (0 == iNumInstrument || iNumInstrument != adblRepo.length) return null;

		for (int i = 0; i <= iNumInstrument; ++i) {
			aiBasisPredictorOrdinate[i] = 0 == i ? dtSpot.julian() : aiDate[i - 1];

			adblBasisResponseValue[i] = 0 == i ? adblRepo[0] : adblRepo[i - 1];

			if (0 != i) aSCBC[i - 1] = scbc;
		}

		try {
			return new org.drip.state.curve.BasisSplineRepoCurve (comp, new
				org.drip.spline.grid.OverlappingStretchSpan
					(org.drip.spline.stretch.MultiSegmentSequenceBuilder.CreateCalibratedStretchEstimator
						(strName, aiBasisPredictorOrdinate, adblBasisResponseValue, aSCBC, null,
							org.drip.spline.stretch.BoundarySettings.NaturalStandard(),
								org.drip.spline.stretch.MultiSegmentSequence.CALIBRATE)));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Create an Instance of the Cubic Polynomial Splined Repo Curve
	 * 
	 * @param strName Curve Name
	 * @param dtSpot The Spot Date
	 * @param comp The Underlying Repo Component
	 * @param aiDate Array of the Dates
	 * @param adblRepo Array of the Repo Rates
	 * 
	 * @return The Instance of the Basis Curve
	 */

	public static final org.drip.state.repo.RepoCurve CubicPolynomialRepoCurve (
		final java.lang.String strName,
		final org.drip.analytics.date.JulianDate dtSpot,
		final org.drip.product.definition.Component comp,
		final int[] aiDate,
		final double[] adblRepo)
	{
		try {
			return CustomSplineRepoCurve (strName, dtSpot, comp, aiDate, adblRepo, new
				org.drip.spline.params.SegmentCustomBuilderControl
					(org.drip.spline.stretch.MultiSegmentSequenceBuilder.BASIS_SPLINE_POLYNOMIAL, new
						org.drip.spline.basis.PolynomialFunctionSetParams (4),
							org.drip.spline.params.SegmentInelasticDesignControl.Create (2, 2), null, null));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Create an Instance of the Quartic Polynomial Splined Repo Curve
	 * 
	 * @param strName Curve Name
	 * @param dtSpot The Spot Date
	 * @param comp The Underlying Repo Component
	 * @param aiDate Array of the Dates
	 * @param adblRepo Array of the Repo Rates
	 * 
	 * @return The Instance of the Splined Repo Curve
	 */

	public static final org.drip.state.repo.RepoCurve QuarticPolynomialRepoCurve (
		final java.lang.String strName,
		final org.drip.analytics.date.JulianDate dtSpot,
		final org.drip.product.definition.Component comp,
		final int[] aiDate,
		final double[] adblRepo)
	{
		try {
			return CustomSplineRepoCurve (strName, dtSpot, comp, aiDate, adblRepo, new
				org.drip.spline.params.SegmentCustomBuilderControl
					(org.drip.spline.stretch.MultiSegmentSequenceBuilder.BASIS_SPLINE_POLYNOMIAL, new
						org.drip.spline.basis.PolynomialFunctionSetParams (5),
							org.drip.spline.params.SegmentInelasticDesignControl.Create (2, 2), null, null));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Create an Instance of the Kaklis-Pandelis Splined Repo Curve
	 * 
	 * @param strName Curve Name
	 * @param dtSpot The Spot Date
	 * @param comp The Underlying Repo Component
	 * @param aiDate Array of the Dates
	 * @param adblRepo Array of the Repo Rates
	 * 
	 * @return The Instance of the Splined Repo Curve
	 */

	public static final org.drip.state.repo.RepoCurve KaklisPandelisRepoCurve (
		final java.lang.String strName,
		final org.drip.analytics.date.JulianDate dtSpot,
		final org.drip.product.definition.Component comp,
		final int[] aiDate,
		final double[] adblRepo)
	{
		try {
			return CustomSplineRepoCurve (strName, dtSpot, comp, aiDate, adblRepo, new
				org.drip.spline.params.SegmentCustomBuilderControl
					(org.drip.spline.stretch.MultiSegmentSequenceBuilder.BASIS_SPLINE_KAKLIS_PANDELIS, new
						org.drip.spline.basis.KaklisPandelisSetParams (2),
							org.drip.spline.params.SegmentInelasticDesignControl.Create (2, 2), null, null));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Create an Instance of the KLK Hyperbolic Splined Repo Curve
	 * 
	 * @param strName Curve Name
	 * @param dtSpot The Spot Date
	 * @param comp The Underlying Repo Component
	 * @param aiDate Array of the Dates
	 * @param adblRepo Array of the Repo Rates
	 * @param dblTension The Tension Parameter
	 * 
	 * @return The Instance of the Splined Repo Curve
	 */

	public static final org.drip.state.repo.RepoCurve KLKHyperbolicRepoCurve (
		final java.lang.String strName,
		final org.drip.analytics.date.JulianDate dtSpot,
		final org.drip.product.definition.Component comp,
		final int[] aiDate,
		final double[] adblRepo,
		final double dblTension)
	{
		try {
			return CustomSplineRepoCurve (strName, dtSpot, comp, aiDate, adblRepo, new
				org.drip.spline.params.SegmentCustomBuilderControl
					(org.drip.spline.stretch.MultiSegmentSequenceBuilder.BASIS_SPLINE_KLK_HYPERBOLIC_TENSION,
						new org.drip.spline.basis.ExponentialTensionSetParams (dblTension),
							org.drip.spline.params.SegmentInelasticDesignControl.Create (2, 2), null, null));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Create an Instance of the KLK Rational Linear Splined Repo Curve
	 * 
	 * @param strName Curve Name
	 * @param dtSpot The Spot Date
	 * @param comp The Underlying Repo Component
	 * @param aiDate Array of the Dates
	 * @param adblRepo Array of the Repo Rates
	 * @param dblTension The Tension Parameter
	 * 
	 * @return The Instance of the Repo Curve
	 */

	public static final org.drip.state.repo.RepoCurve KLKRationalLinearRepoCurve (
		final java.lang.String strName,
		final org.drip.analytics.date.JulianDate dtSpot,
		final org.drip.product.definition.Component comp,
		final int[] aiDate,
		final double[] adblRepo,
		final double dblTension)
	{
		try {
			return CustomSplineRepoCurve (strName, dtSpot, comp, aiDate, adblRepo, new
				org.drip.spline.params.SegmentCustomBuilderControl
					(org.drip.spline.stretch.MultiSegmentSequenceBuilder.BASIS_SPLINE_KLK_RATIONAL_LINEAR_TENSION,
				new org.drip.spline.basis.ExponentialTensionSetParams (dblTension),
					org.drip.spline.params.SegmentInelasticDesignControl.Create (2, 2), null, null));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Create an Instance of the KLK Rational Quadratic Splined Repo Curve
	 * 
	 * @param strName Curve Name
	 * @param dtSpot The Spot Date
	 * @param comp The Underlying Repo Component
	 * @param aiDate Array of the Dates
	 * @param adblRepo Array of the Repo Rates
	 * @param dblTension The Tension Parameter
	 * 
	 * @return The Instance of the Repo Curve
	 */

	public static final org.drip.state.repo.RepoCurve KLKRationalQuadraticRepoCurve (
		final java.lang.String strName,
		final org.drip.analytics.date.JulianDate dtSpot,
		final org.drip.product.definition.Component comp,
		final int[] aiDate,
		final double[] adblRepo,
		final double dblTension)
	{
		try {
			return CustomSplineRepoCurve (strName, dtSpot, comp, aiDate, adblRepo, new
				org.drip.spline.params.SegmentCustomBuilderControl
					(org.drip.spline.stretch.MultiSegmentSequenceBuilder.BASIS_SPLINE_KLK_RATIONAL_QUADRATIC_TENSION,
				new org.drip.spline.basis.ExponentialTensionSetParams (dblTension),
					org.drip.spline.params.SegmentInelasticDesignControl.Create (2, 2), null, null));
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Construct a Repo Curve using the Flat Repo Rate
	 * 
	 * @param dtSpot Spot Date
	 * @param comp Repo Component
	 * @param dblRepoRate The Flat Repo Rate
	 * 
	 * @return The Flat Repo Rate Curve
	 */

	public static final org.drip.state.repo.RepoCurve FlatRateRepoCurve (
		final org.drip.analytics.date.JulianDate dtSpot,
		final org.drip.product.definition.Component comp,
		final double dblRepoRate)
	{
		if (null == dtSpot) return null;

		int iEpochDate = dtSpot.julian();

		try {
			return new org.drip.state.nonlinear.FlatForwardRepoCurve (iEpochDate, comp, new int[]
				{iEpochDate}, new double[] {dblRepoRate});
		} catch (java.lang.Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
