
package org.drip.spaces.graph;

/*
 * -*- mode: java; tab-width: 4; indent-tabs-mode: nil; c-basic-offset: 4 -*-
 */

/*!
 * Copyright (C) 2018 Lakshmi Krishnamurthy
 * 
 *  This file is part of DRIP, a free-software/open-source library for buy/side financial/trading model
 *  	libraries targeting analysts and developers
 *  	https://lakshmidrip.github.io/DRIP/
 *  
 *  DRIP is composed of four main libraries:
 *  
 *  - DRIP Fixed Income - https://lakshmidrip.github.io/DRIP-Fixed-Income/
 *  - DRIP Asset Allocation - https://lakshmidrip.github.io/DRIP-Asset-Allocation/
 *  - DRIP Numerical Optimizer - https://lakshmidrip.github.io/DRIP-Numerical-Optimizer/
 *  - DRIP Statistical Learning - https://lakshmidrip.github.io/DRIP-Statistical-Learning/
 * 
 *  - DRIP Fixed Income: Library for Instrument/Trading Conventions, Treasury Futures/Options,
 *  	Funding/Forward/Overnight Curves, Multi-Curve Construction/Valuation, Collateral Valuation and XVA
 *  	Metric Generation, Calibration and Hedge Attributions, Statistical Curve Construction, Bond RV
 *  	Metrics, Stochastic Evolution and Option Pricing, Interest Rate Dynamics and Option Pricing, LMM
 *  	Extensions/Calibrations/Greeks, Algorithmic Differentiation, and Asset Backed Models and Analytics.
 * 
 *  - DRIP Asset Allocation: Library for model libraries for MPT framework, Black Litterman Strategy
 *  	Incorporator, Holdings Constraint, and Transaction Costs.
 * 
 *  - DRIP Numerical Optimizer: Library for Numerical Optimization and Spline Functionality.
 * 
 *  - DRIP Statistical Learning: Library for Statistical Evaluation and Machine Learning.
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
 * ShortestPathVertex holds the given Vertex's Previous Traversal Vertex and the Weight from the Source. The
 *  References are:
 *  
 *  1) Wikipedia (2018a): Graph (Abstract Data Type)
 *  	https://en.wikipedia.org/wiki/Graph_(abstract_data_type).
 *  
 *  2) Wikipedia (2018b): Graph Theory https://en.wikipedia.org/wiki/Graph_theory.
 *  
 *  3) Wikipedia (2018c): Graph (Discrete Mathematics)
 *  	https://en.wikipedia.org/wiki/Graph_(discrete_mathematics).
 *  
 *  4) Wikipedia (2018d): Dijkstra's Algorithm https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm.
 *  
 *  5) Wikipedia (2018e): Bellman-Ford Algorithm
 *  	https://en.wikipedia.org/wiki/Bellman%E2%80%93Ford_algorithm.
 *
 * @author Lakshmi Krishnamurthy
 */

public class ShortestPathVertex
{
	private boolean _visited = false;
	private java.lang.String _current = "";
	private java.lang.String _preceeding = "";
	private double _weightFromSource = java.lang.Double.POSITIVE_INFINITY;

	/**
	 * ShortestPathVertex Constructor
	 * 
	 * @param current The Current Node
	 * 
	 * @throws java.lang.Exception Thrown if the Inputs are Invalid
	 */

	public ShortestPathVertex (
		final java.lang.String current)
		throws java.lang.Exception
	{
		if (null == (_current = current) || _current.isEmpty())
		{
			throw new java.lang.Exception ("ShortestPathVertex Constructor => Invalid Inputs");
		}
	}

	/**
	 * Retrieve the Current Vertex
	 * 
	 * @return The Current Vertex
	 */

	public java.lang.String current()
	{
		return _current;
	}

	/**
	 * Retrieve the Preceeding Traversal Vertex
	 * 
	 * @return The Preceeding Traversal Vertex
	 */

	public java.lang.String preceeding()
	{
		return _preceeding;
	}

	/**
	 * Set the Preceeding Traversal Vertex
	 * 
	 * @param preceeding The Preceeding Traversal Vertex
	 * 
	 * @return TRUE - The Preceeding Vertex successfully set
	 */

	public boolean setPreceeding (
		final java.lang.String preceeding)
	{
		if (null == preceeding || preceeding.isEmpty())
		{
			return false;
		}

		_preceeding = preceeding;
		return true;
	}

	/**
	 * Retrieve the Weight From the Source
	 * 
	 * @return The Weight From the Source
	 */

	public double weightFromSource()
	{
		return _weightFromSource;
	}

	/**
	 * Set the Weight From Source
	 * 
	 * @param weightFromSource The Weight From Source
	 * 
	 * @return TRUE - The Weight From Source successfully set
	 */

	public boolean setWeightFromSource (
		final double weightFromSource)
	{
		if (!org.drip.quant.common.NumberUtil.IsValid (weightFromSource))
		{
			return false;
		}

		_weightFromSource = weightFromSource;
		return true;
	}

	/**
	 * Indicate if the Vertex has been Visited
	 * 
	 * @return TRUE - The Vertex has been Visited
	 */
	
	public boolean visited()
	{
		return _visited;
	}

	/**
	 * Set the Visitation Status of the Vertex
	 * 
	 * @param visited The Visitation Status
	 * 
	 * @return TRUE - The Visitation Status successfully set
	 */

	public boolean setVisited (
		final boolean visited)
	{
		_visited = visited;
		return true;
	}
}