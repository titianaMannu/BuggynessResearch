#!/bin/bash
# Bash Menu Script Example

PS3='Please enter your choice: '
options=("Metrics Calculator" "Walk Forward" "Quit")
echo "1 : Use Metrics Calculator - milestone 1 of deliverable2"
echo "2 : Use Walk Forward - milestone 2 of deliverable2"
echo "3 : Quit"
tar -xf ../openjpa-cache.tar.xz -C ../
tar -xf ../bookkeeper-cache.tar.xz -C ../
select opt in "${options[@]}"
do
    case $opt in
        "Metrics Calculator")
            cd ../ && java -cp BuggynessResearch-1.0-SNAPSHOT-jar-with-dependencies.jar main.MetricsCalculator
            cd bin/
            ;;
        "Walk Forward")
            cd ../ && java -cp BuggynessResearch-1.0-SNAPSHOT-jar-with-dependencies.jar classification.walk_forward.WalkForward
            cd bin/
            ;;
          "Quit")
            break
            ;;
        *) echo "invalid option $REPLY";;
    esac
    echo "1 : Use Metrics Calculator - milestone 1 of deliverable2"
    echo "2 : Use Walk Forward - milestone 2 of deliverable2"
    echo "3 : Quit"

done
