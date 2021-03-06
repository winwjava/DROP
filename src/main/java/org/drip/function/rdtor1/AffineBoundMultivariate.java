
package org.drip.function.rdtor1;

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
 * <i>AffineBoundMultivariate</i> implements a Bounded Planar Linear R<sup>d</sup> To R<sup>1</sup> Function.
 *
 *	<br><br>
 *  <ul>
 *		<li><b>Module </b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/NumericalCore.md">Numerical Core Module</a></li>
 *		<li><b>Library</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/NumericalOptimizerLibrary.md">Numerical Optimizer</a></li>
 *		<li><b>Project</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/feed/README.md">Function</a></li>
 *		<li><b>Package</b> = <a href = "https://github.com/lakshmiDRIP/DROP/tree/master/src/main/java/org/drip/feed/rdtor1/README.md">R<sup>d</sup> To R<sup>1</sup></a></li>
 *  </ul>
 *
 * @author Lakshmi Krishnamurthy
 */

public class AffineBoundMultivariate extends org.drip.function.definition.RdToR1 implements
	org.drip.function.rdtor1.BoundMultivariate, org.drip.function.rdtor1.ConvexMultivariate {
	private boolean _bIsUpper = false;
	private int _iNumTotalVariate = -1;
	private int _iBoundVariateIndex = -1;
	private double _dblBoundValue = java.lang.Double.NaN;

	/**
	 * AffineBoundMultivariate Constructor
	 * 
	 * @param bIsUpper TRUE To The Bound is an Upper Bound
	 * @param iBoundVariateIndex The Bound Variate Index
	 * @param iNumTotalVariate The Total Number of Variates
	 * @param dblBoundValue The Bounding Value
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public AffineBoundMultivariate (
		final boolean bIsUpper,
		final int iBoundVariateIndex,
		final int iNumTotalVariate,
		final double dblBoundValue)
		throws java.lang.Exception
	{
		super (null);

		if (!org.drip.numerical.common.NumberUtil.IsValid (_dblBoundValue = dblBoundValue) || 0 ==
			(_iNumTotalVariate = iNumTotalVariate) || _iNumTotalVariate <= (_iBoundVariateIndex =
				iBoundVariateIndex))
			throw new java.lang.Exception ("AffineBoundMultivariate Constructor => Invalid Inputs");

		_bIsUpper = bIsUpper;
	}

	@Override public boolean isUpper()
	{
		return _bIsUpper;
	}

	@Override public int boundVariateIndex()
	{
		return _iBoundVariateIndex;
	}

	@Override public double boundValue()
	{
		return _dblBoundValue;
	}

	@Override public boolean violated (
		final double dblVariate)
		throws java.lang.Exception
	{
		if (!org.drip.numerical.common.NumberUtil.IsValid (dblVariate))
			throw new java.lang.Exception ("AffineBoundMultivariate::violated => Invalid Inputs");

		if (_bIsUpper && dblVariate > _dblBoundValue) return true;

		if (!_bIsUpper && dblVariate < _dblBoundValue) return true;

		return false;
	}

	@Override public int dimension()
	{
		return _iNumTotalVariate;
	}

	@Override public double evaluate (
		final double[] adblVariate)
		throws java.lang.Exception
	{
		if (null == adblVariate || !org.drip.numerical.common.NumberUtil.IsValid (adblVariate) ||
			adblVariate.length != dimension())
			throw new java.lang.Exception ("AffineBoundMultivariate::evaluate => Invalid Inputs");

		return _bIsUpper ? _dblBoundValue - adblVariate[_iBoundVariateIndex] :
			adblVariate[_iBoundVariateIndex] - _dblBoundValue;
	}

	@Override public double[] jacobian (
		final double[] adblVariate)
	{
		double[] adblJacobian = new double[_iNumTotalVariate];

		for (int i = 0; i < _iNumTotalVariate; ++i)
			adblJacobian[i] = i == _iBoundVariateIndex ? (_bIsUpper ? -1. : 1.) : 0.;

		return adblJacobian;
	}

	@Override public double[][] hessian (
		final double[] adblVariate)
	{
		int iDimension = dimension();

		double[][] aadblHessian = new double[iDimension][iDimension];

		for (int i = 0; i < iDimension; ++i) {
			for (int j = 0; j < iDimension; ++j)
				aadblHessian[i][j] = 0.;
		}

		return aadblHessian;
	}
}
