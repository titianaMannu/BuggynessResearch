#!/bin/bash
# Bash Menu Script Example

PS3='Please enter your choice: '
options=("Metrics Calculator" "Walk Forward" "Quit")

echo "processing cache..."
tar -xf ../openjpa-cache.tar.xz -C ../
tar -xf ../bookkeeper-cache.tar.xz -C ../
echo "1 : Use Metrics Calculator - milestone 1 of deliverable2"
echo "2 : Use Walk Forward - milestone 2 of deliverable2"
echo "3 : Quit"
select opt in "${options[@]}"
do
    case $opt in
        "Metrics Calculator")
            cd ../ && java -cp BuggynessResearch-1.0-SNAPSHOT-jar-with-dependencies.jar main.MetricsCalculator
            cd bin/
            ;;
        "Walk Forward")
            if [ !  -f "../BOOKKEEPER_metrics.csv" ]
              then
                cp ../resources/BOOKKEEPER_metrics.csv ../
              fi
             if [ ! -f "../OPENJPA_metrics.csv" ]
              then
                cp ../resources/OPENJPA_metrics.csv ../
             fi
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
