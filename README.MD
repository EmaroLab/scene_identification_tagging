# Scene Identification & Tagging (SIT)

A semantic algorithm to learn and recognise composition ob objects based on their qualitative spatial relations.
The algorithm is presented [here](https://ieeexplore.ieee.org/abstract/document/8956457/) and used for Human-Robot Interaction [here](https://ieeexplore.ieee.org/abstract/document/8525828). 
Also, an implementation based on Fuzzy Reasoning is available [here](https://github.com/EmaroLab/fuzzy_sit) and presented [here](http://ceur-ws.org/Vol-2054/paper2.pdf)

## Algorithm

The algorithm is based on an OWL ontology, the Pellet reasoner and couple of mapping functions.
Its main purposes is to describe primitive objects though geometric coefficients that
define their shape. Than, qualitative spatial relations, such as: *right/left*, *front/behind*, *above/below*,
*parallel*, *perpendicular*, *coaxial*, *connected* are symbolically computed. Those are mapped in an concrete scene
representation (i.e.: an individual). The recognition phase is based on instance checking by looking
for the abstract scene representation (i.e.: classes) that classify the scene individual.
If those does not exist the algorithm is able to use the concrete scene as a template to 
learn its abstract class to be used for further classification. Noteworthy, the
system is automatically able to reason about similarity between learned scene. 
Also, it can be the case that a very complex scene is recognised by a relative small number of relation
that hold (i.e.: a sub scene). To discriminate when those differences are too height, and
trigger a new learning procedure, the concept of *confidence* is introduce as a number within [0,1].

## Dependences
 
This implementation depends on [OWLOOP](https://github.com/EmaroLab/owloop). 
It is implemented within the Robotic Operative System (ROS) Java bridge.
The compilation is based on Gradle and you can find a complete list of dependencies versions 
in the [buld.gradle](https://github.com/EmaroLab/scene_identification_tagging/blob/sit_owloop/sit/build.gradle) file.

## Examples and Getting Started

For a quick get starting tutorial, please, check out our [examples](https://github.com/EmaroLab/scene_identification_tagging/tree/master/sit/src/main/java/it/emarolab/sit/example).
 
### Contacts

Work are progressing to give you more details about the algorithm.
In the middle while, for any information, support, discussion or comments do not hesitate to contact me through this Github repository or at: 
[luca.buoncompagni@edu.unige.it](mailto:luca.buoncompagni@edu.unige.it), 
