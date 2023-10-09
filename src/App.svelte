<script lang="ts">
    import { Router, Route } from "svelte-routing";
    import Login from "@routes/Login.svelte";
    import Register from "@routes/Register.svelte";
    import Home from "@routes/Home.svelte";
    import Profile from "@routes/Profile.svelte";
</script>

<Router>
  <Route path="/" component={Home}/>
  <Route path="/login" component={Login}/>
  <Route path="/register" component={Register}/>
  <Route path="/profile" component={Profile}/>
</Router>