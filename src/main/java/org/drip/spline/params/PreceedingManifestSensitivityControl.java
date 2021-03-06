
package org.drip.spline.params;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2019 Lakshmi Krishnamurthy
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * Copyright (C) 2017 Lakshmi Krishnamurthy
 * Copyright (C) 2016 Lakshmi Krishnamurthy
 * Copyright (C) 2015 Lakshmi Krishnamurthy
 * Copyright (C) 2014 Lakshmi Krishnamurthy
 * Copyright (C) 2013 Lakshmi Krishnamurthy
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
 * <i>PreceedingManifestSensitivityControl</i> provides the control parameters that determine the behavior of
 * non local manifest sensitivity.
 *
 * <br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/NumericalCore.md">Numerical Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/SplineBuilderLibrary.md">Spline Builder Library</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/spline">Spline</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/spline/params">Params</a></li>
 *  </ul>
 * <br><br>
 *
 * @author Lakshmi Krishnamurthy
 */

public class PreceedingManifestSensitivityControl {
	private boolean _bImpactFade = false;
	private int _iCkDBasisCoeffDPreceedingManifest = 0;
	private org.drip.spline.segment.BasisEvaluator _be = null;

	/**
	 * PreceedingManifestSensitivityControl constructor
	 * 
	 * @param bImpactFade TRUE - Fade the Manifest Sensitivity Impact; FALSE - Retain it
	 * @param iCkDBasisCoeffDPreceedingManifest Ck of DBasisCoeffDPreceedingManifest
	 * @param be Basis Evaluator Instance
	 * 
	 * @throws java.lang.Exception Thrown if Inputs are invalid
	 */

	public PreceedingManifestSensitivityControl (
		final boolean bImpactFade,
		final int iCkDBasisCoeffDPreceedingManifest,
		final org.drip.spline.segment.BasisEvaluator be)
		throws java.lang.Exception
	{
		if (0 > (_iCkDBasisCoeffDPreceedingManifest = iCkDBasisCoeffDPreceedingManifest))
			throw new java.lang.Exception ("PreceedingManifestSensitivityControl ctr: Invalid Inputs");

		_be = be;
		_bImpactFade = bImpactFade;
	}

	/**
	 * Retrieve the Ck of DBasisCoeffDPreceedingManifest
	 * 
	 * @return Ck of DBasisCoeffDPreceedingManifest
	 */

	public int Ck()
	{
		return _iCkDBasisCoeffDPreceedingManifest;
	}

	/**
	 * Retrieve the Basis Evaluator Instance
	 * 
	 * @return The Basis Evaluator Instance
	 */

	public org.drip.spline.segment.BasisEvaluator basisEvaluator()
	{
		return _be;
	}

	/**
	 * Retrieve the Preceeding Manifest Measure Impact Flag
	 * 
	 * @return The Preceeding Manifest Measure Impact Flag
	 */

	public boolean impactFade()
	{
		return _bImpactFade;
	}
}
