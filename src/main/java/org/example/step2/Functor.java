package org.example.step2;

/**

 <pre>
 interface Functor&lt;F&gt; {
     // (a -&gt; b) -&gt; f a -&gt; f b
     &lt;A, B&gt; F&lt;B&gt; map(Function&lt;A, B&gt; mapper, F&lt;A&gt; cont); // &lt;&lt;-- Error: Type 'F' does not have type parameters.
 }
 </pre>
*/
interface Functor {}
