# Buggy Research

# Introduction

In this repository it is implemented a simple and fast method to retrieve information on open-source apache projects by
using REST APIs in order to produce a dataSet containing information about per-releases files.

If there aren't any Affected versions from Jira, "Proportion" method with moving window is used to classify files as buggy or not.
Additional feature as LOC, size, age, changeSetSize,  number of authors and so on are calculated and included to the dataSet.

The dataset is used as input for machine learning algorithms. Weka APIs are used to study what combinations of techniques (SMOTE, underSampling, overSampling, best first...)
increases classifier's (naiveBayes, Random Forest, Ibk) accuracy. 

# Structure

- main: includes main classes to build the dataSet. 
- internal.data_scraping: includes classes useful for retrieve infos by using REST APIs  (GitHub and Jira)
- classification.walkForward: includes classes for machine learning algorithms. Walk forward time-series technique is used to reduce random noise from predictions.

# Usage

Go to bin/ directory and use the following command

    /bin/bash init.sh